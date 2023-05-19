package ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.Credentials
import ru.ilya.messenger.databinding.FragmentAllStreamsBinding
import ru.ilya.messenger.di.getAppComponent
import ru.ilya.messenger.domain.entities.TopicData
import ru.ilya.messenger.presentation.ui.channels.ChannelsFragment
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.StreamsAdapter
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.AllStreamsStoreFactory
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsEffect
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsEvent
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsState
import ru.ilya.messenger.presentation.ui.chat.MessageChatFragment
import ru.ilya.messenger.util.Constants
import ru.ilya.messenger.util.mapListStreamAllModelToListExpandableItem
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class AllStreamsFragment :
    ElmFragment<AllStreamsEvent, AllStreamsEffect, AllStreamsState>() {

    private val streamAdapter: StreamsAdapter by lazy { StreamsAdapter() }
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var onTopicClickListenerToOpenChatFromAllStreams
            : OnTopicClickListenerToOpenChatFromAllStreams

    private var _binding: FragmentAllStreamsBinding? = null
    private val binding: FragmentAllStreamsBinding
        get() = _binding ?: throw RuntimeException("FragmentAllStreamsBinding == null")

    @Inject
    lateinit var allStreamsStoreFactory: AllStreamsStoreFactory


    fun updateStreamsList(value: String) {
        lifecycleScope.launch {
            val eventSearch = AllStreamsEvent.Ui.LoadStreamBySearch(
                auth = Credentials.basic(
                    Constants.EMAIL,
                    Constants.PASSWORD
                ), query = value
            )
            value.let { store.accept(eventSearch) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTopicClickListenerToOpenChatFromAllStreams) {
            onTopicClickListenerToOpenChatFromAllStreams = context
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().getAppComponent().inject(this)
        super.onCreate(savedInstanceState)

        // загружаем стримы из БД
        val eventToLoadStreamsFromDb = AllStreamsEvent.Ui.LoadStreamsFromDb
        store.accept(eventToLoadStreamsFromDb)
        val eventToLoadStreams = AllStreamsEvent.Ui.LoadStreams
        store.accept(eventToLoadStreams)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun setupRecyclerView() {
        with(binding.recyclerViewAllStreams) {
            layoutManager = LinearLayoutManager(activity)
            adapter = streamAdapter

            streamAdapter.onTopicClickListener = {
                val streamTitleFromSP = sharedPreferences
                    .getString(
                        ChannelsFragment.KEY_STREAM_TITLE,
                        ChannelsFragment.DEFAULT_STREAM_TITLE
                    ) ?: ChannelsFragment.EMPTY_STREAM_TITLE
                val streamIdFromSP = sharedPreferences
                    .getInt(
                        ChannelsFragment.KEY_STREAM_ID,
                        ChannelsFragment.DEFAULT_STREAM_ID
                    )
                val fragment = launchChatFragment(it, streamTitleFromSP, streamIdFromSP)

                // запуск фрагмента с чатом по клику на топик
                onTopicClickListenerToOpenChatFromAllStreams.onTopicClickedFromAllStreams(fragment)
            }

            streamAdapter.onArrowClickListener = { streamName, streamId ->
                // по клику на стрелку у стрима, сохраняем его название и id в shared preferences
                // когда нажмем на топик, то из shared preferences возьмем тот самый стрим,
                // на который нажали в последний раз и в launchChatFragment передадим оба параметра
                sharedPreferences = context.getSharedPreferences(
                    ChannelsFragment.SHARED_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.putString(ChannelsFragment.KEY_STREAM_TITLE, streamName).apply()
                editor.putInt(ChannelsFragment.KEY_STREAM_ID, streamId).apply()
            }
        }

    }


    interface OnTopicClickListenerToOpenChatFromAllStreams {
        fun onTopicClickedFromAllStreams(fragment: Fragment)
    }

    private fun showSnackBarError(message: String) {
        // прячем клавиатуру
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        val snack = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val view: View = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_VERTICAL
        view.layoutParams = params
        snack.show()
    }

    private fun launchChatFragment(
        topicObject: TopicData,
        streamTitle: String,
        streamId: Int
    ): MessageChatFragment {
        return MessageChatFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ChannelsFragment.KEY_TOPIC_NAME, topicObject)
                putString(ChannelsFragment.KEY_STREAM_NAME, streamTitle)
                putInt(ChannelsFragment.KEY_STREAM_ID, streamId)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val initEvent: AllStreamsEvent = AllStreamsEvent.Ui.LoadStreams

    override fun createStore(): Store<AllStreamsEvent, AllStreamsEffect, AllStreamsState> {
        return allStreamsStoreFactory.provide()
    }

    override fun render(state: AllStreamsState) {
        with(binding) {
            if (state.isLoading) {
                shimmerLayout.startShimmer()
                shimmerLayout.visibility = View.VISIBLE
            }
            if (!state.isLoading) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
            // если бд пустая и ответ с бэка еще не пришел - показываем шиммер
            if (state.streams.isEmpty() && state.streamFromServer.isEmpty()) {
                shimmerLayout.startShimmer()
                shimmerLayout.visibility = View.VISIBLE
                streamAdapter.items = emptyList()
            }
            if (state.streams.isNotEmpty()) {
                streamAdapter.items = mapListStreamAllModelToListExpandableItem(state.streams)
            }
            if (state.streamFromServer.isNotEmpty()) {
                streamAdapter.items =
                    mapListStreamAllModelToListExpandableItem(state.streamFromServer)
                // когда приходят актуальные данные с бэка, обновляем нашу бд
                val eventToUpdateDb =
                    AllStreamsEvent.Ui.AddStreamsFromServerToDb(state.streamFromServer) // обновляем стримы в бд
                store.accept(eventToUpdateDb)
            }

            state.error?.let {
                showSnackBarError("Ошибка: ${state.error.localizedMessage}")
            }
        }
    }

    override fun handleEffect(effect: AllStreamsEffect) = when (effect) {
        is AllStreamsEffect.AllStreamsError -> {
            showSnackBarError(effect.errorMessage)
        }
    }

}
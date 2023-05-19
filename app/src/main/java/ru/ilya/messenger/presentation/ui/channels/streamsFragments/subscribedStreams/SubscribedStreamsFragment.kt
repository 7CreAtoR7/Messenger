package ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams

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
import ru.ilya.messenger.databinding.FragmentSubscribedStreamsBinding
import ru.ilya.messenger.di.getAppComponent
import ru.ilya.messenger.domain.entities.TopicData
import ru.ilya.messenger.presentation.ui.channels.ChannelsFragment
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.StreamsAdapter
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.SubscribedStoreFactory
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedEffect
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedEvent
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedState
import ru.ilya.messenger.presentation.ui.chat.MessageChatFragment
import ru.ilya.messenger.util.Constants
import ru.ilya.messenger.util.mapListStreamAllModelToListExpandableItem
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


class SubscribedStreamsFragment :
    ElmFragment<SubscribedEvent, SubscribedEffect, SubscribedState>() {
    private val streamAdapter: StreamsAdapter by lazy { StreamsAdapter() }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var onTopicClickListenerToOpenChatFromSubscribedStreams:
            OnTopicClickListenerToOpenChatFromSubscribedStreams

    private var _binding: FragmentSubscribedStreamsBinding? = null
    private val binding: FragmentSubscribedStreamsBinding
        get() = _binding ?: throw RuntimeException("FragmentSubscribedStreamsBinding == null")

    @Inject
    lateinit var subscribedStoreFactory: SubscribedStoreFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTopicClickListenerToOpenChatFromSubscribedStreams) {
            onTopicClickListenerToOpenChatFromSubscribedStreams = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().getAppComponent().inject(this)
        super.onCreate(savedInstanceState)

        // загружаем стримы из БД
        val eventToLoadStreamsFromDb = SubscribedEvent.Ui.LoadStreamsFromDb
        store.accept(eventToLoadStreamsFromDb)
        // загружаем стримы из бэка
        val eventToLoadStreams = SubscribedEvent.Ui.LoadStreams
        store.accept(eventToLoadStreams)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribedStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    fun updateStreamsList(value: String) {
        lifecycleScope.launch {
            val eventSearch = SubscribedEvent.Ui.LoadStreamBySearch(
                auth = Credentials.basic(
                    Constants.EMAIL,
                    Constants.PASSWORD
                ), query = value
            )
            value.let { store.accept(eventSearch) }
        }
    }

    interface OnTopicClickListenerToOpenChatFromSubscribedStreams {
        fun onTopicClickedFromSubscribedStreams(fragment: Fragment)
    }

    private fun setupRecyclerView() {
        with(binding.recyclerViewSubscribedStreams) {
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
                onTopicClickListenerToOpenChatFromSubscribedStreams
                    .onTopicClickedFromSubscribedStreams(fragment)
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

    override val initEvent: SubscribedEvent
        get() = SubscribedEvent.Ui.LoadStreams

    override fun createStore(): Store<SubscribedEvent, SubscribedEffect, SubscribedState> {
        return subscribedStoreFactory.provide()
    }

    override fun render(state: SubscribedState) {
        with(binding) {
            if (state.isLoading) {
                shimmerLayout.startShimmer()
                shimmerLayout.visibility = View.VISIBLE
            }
            if (!state.isLoading) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
            if (state.isLoadingSearch) {
                LoaderOfSearching.visibility = View.VISIBLE
            }
            if (!state.isLoadingSearch) {
                LoaderOfSearching.visibility = View.GONE
            }
            // если бд пустая и ответ с бэка еще не пришел - показываем шиммер
            if (state.streams.isEmpty() && state.streamFromServer.isEmpty()) {
                shimmerLayout.startShimmer()
                shimmerLayout.visibility = View.VISIBLE
                streamAdapter.items = emptyList()
            }
            if (state.streams.isNotEmpty()) {
                streamAdapter.items =
                    mapListStreamAllModelToListExpandableItem(state.streams.map { streamData -> streamData.toStreamAllModel() })
            }
            if (state.streamFromServer.isNotEmpty()) {
                streamAdapter.items =
                    mapListStreamAllModelToListExpandableItem(state.streamFromServer.map { streamData -> streamData.toStreamAllModel() })
                // когда приходят актуальные данные с бэка, обновляем нашу бд
                val eventToUpdateDb =
                    SubscribedEvent.Ui.AddStreamsFromServerToDb(state.streamFromServer) // обновляем стримы в бд
                store.accept(eventToUpdateDb)
            }

            state.error?.let {
                showSnackBarError("Ошибка: ${state.error.localizedMessage}")
            }
        }
    }

    override fun handleEffect(effect: SubscribedEffect) = when (effect) {
        is SubscribedEffect.SubscribedStreamsError -> {
            showSnackBarError(effect.errorMessage)
        }
    }

}
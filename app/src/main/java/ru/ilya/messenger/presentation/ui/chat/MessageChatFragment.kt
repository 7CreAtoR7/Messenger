package ru.ilya.messenger.presentation.ui.chat

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Credentials
import ru.ilya.messenger.databinding.FragmentSingleChatBinding
import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.domain.entities.TopicData
import ru.ilya.messenger.presentation.customViewsEmojis.ReactionView
import ru.ilya.messenger.presentation.ui.MainActivity
import ru.ilya.messenger.presentation.ui.channels.ChannelsFragment
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.date.DateDelegate
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.date.DateModel
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.myMessage.MyMessageDelegate
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.notMyMessage.NotMyMessageDelegate
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.DelegateItem
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.MessageChatAdapterDelegate
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.pagination.PaginationAdapterHelper
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatEffect
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatEvent
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatState
import ru.ilya.messenger.util.Constants
import ru.ilya.messenger.util.timestampToLocalDate
import ru.ilya.messenger.R
import ru.ilya.messenger.di.getAppComponent
import ru.ilya.messenger.presentation.ui.chat.elm.ChatStoreFactory
import ru.ilya.messenger.util.concatenateWithDate
import ru.ilya.messenger.util.formatDateToDayMonthString
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


class MessageChatFragment :
    ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    private val adapter: MessageChatAdapterDelegate by lazy { MessageChatAdapterDelegate() }
    private val authorizationValue = Credentials.basic(Constants.EMAIL, Constants.PASSWORD)
    private lateinit var clickListener: ClickReaction
    private var idLastMessage = 0
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var topicTitle: String
    private lateinit var streamTitle: String
    private var streamId = 0

    private var dateList = mutableListOf<DateModel>()
    private var messageList = mutableListOf<MessageModel>()

    private var _binding: FragmentSingleChatBinding? = null
    private val binding: FragmentSingleChatBinding
        get() = _binding ?: throw RuntimeException("FragmentSingleChatBinding == null")

    @Inject
    lateinit var chatStoreFactory: ChatStoreFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        parseArgs()

        // загружаем сообщения из БД
        val eventToLoadMessagesFromDb = ChatEvent.Ui.LoadMessagesFromDb(streamId)
        store.accept(eventToLoadMessagesFromDb)
        // загружаем сообщения из бэка
        val eventToLoadMessagesFromServer = ChatEvent.Ui.LoadInitMessages(topicTitle, streamTitle)
        store.accept(eventToLoadMessagesFromServer)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMessageTextChangeListener()
        initClickListener()
        initToolBar()
        initAdapter()
    }

    private fun initMessageTextChangeListener() {
        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == MIN_MESSAGE_LENGTH) {
                    binding.btnAddFile.visibility = View.VISIBLE
                    binding.btnSend.visibility = View.INVISIBLE
                } else {
                    binding.btnAddFile.visibility = View.INVISIBLE
                    binding.btnSend.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun initClickListener() {
        // обработка нажатия на эмодзи в bottom sheet
        clickListener = object : ClickReaction {
            override fun onEmojiInBottomSheetClick(messageId: Int, emojiName: String) {
                val eventAddEmoji = ChatEvent.Ui.AddMessageReaction(
                    auth = authorizationValue,
                    messageId = messageId,
                    emojiName = emojiName
                )
                store.accept(eventAddEmoji)
            }

            override fun onEmojiClicked(view: View, messageId: Int) {
                view as ReactionView
                // если выделен мной->счетчик минус 1, и если теперь счетчик=0 -> удаляется эмодзи
                if (view.isSelected) {
                    val eventDeleteEmoji = ChatEvent.Ui.DeleteMessageReaction(
                        auth = authorizationValue,
                        messageId = messageId,
                        emojiName = view.nameOfEmoji
                    )
                    store.accept(eventDeleteEmoji)
                } else {
                    val eventAddEmoji = ChatEvent.Ui.AddMessageReaction(
                        auth = authorizationValue,
                        messageId = messageId,
                        emojiName = view.nameOfEmoji
                    )
                    store.accept(eventAddEmoji)
                }

            }
        }

        binding.btnSend.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val message = binding.etMessage.text.toString()
                val event = ChatEvent.Ui.SendMessage(
                    auth = authorizationValue,
                    type = RECEIVER_TYPE,
                    content = message,
                    to = streamTitle,
                    topic = topicTitle
                )
                store.accept(event)
                binding.etMessage.setText(EMPTY_EDIT_TEXT)
            }
        }

        binding.btnAddFile.setOnClickListener {
            Toast.makeText(activity, "Выбери файл для отправки", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initToolBar() {
        binding.toolbarTitle.text = topicTitle
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initAdapter() {
        binding.chatRecyclerView.adapter = adapter
        adapter.apply {
            addDelegate(MyMessageDelegate(PaginationAdapterHelper {
                store.accept(
                    ChatEvent.Ui.LoadNextMessages(
                        topicTitle,
                        streamTitle
                    )
                )
            }))
            addDelegate(
                NotMyMessageDelegate(
                    clickListener,
                    PaginationAdapterHelper {
                        store.accept(
                            ChatEvent.Ui.LoadNextMessages(
                                topicTitle,
                                streamTitle
                            )
                        )
                    })
            )
            addDelegate(DateDelegate())
        }
    }

    private fun parseArgs() {
        requireArguments().getParcelable<TopicData>(ChannelsFragment.KEY_TOPIC_NAME)?.let {
            topicTitle = it.name
        }
        requireArguments().getString(ChannelsFragment.KEY_STREAM_NAME)?.let {
            streamTitle = it
        }
        requireArguments().getInt(ChannelsFragment.KEY_STREAM_ID).let {
            streamId = it
        }
    }

    private fun putMessageInOurChat(message: MessageModel) {
        // если в списке дат нет такой даты, то добавляем её в RecyclerView:
        val dateMessageSending =
            timestampToLocalDate(message.timestamp).formatDateToDayMonthString()
        val date = DateModel(date = dateMessageSending)
        if (!dateList.contains(date)) {
            dateList.add(DateModel(date = dateMessageSending))
        }
        messageList.add(
            MessageModel(
                id = message.id,
                avatarUrl = message.avatarUrl,
                content = message.content,
                reactions = message.reactions,
                senderEmail = message.senderEmail,
                senderFullName = message.senderFullName,
                senderId = message.senderId,
                streamId = message.streamId,
                timestamp = message.timestamp,
            )
        )
    }

    private fun initView() {
        // есть ли этот фрагмент в списке bottomNavigationView -> показываем bottomNavigationView
        // P.S. в приложении всего 1 активность, так что это безопасный каст
        (requireActivity() as MainActivity)
            .bottomNavigationVisibilityUtils.setBottomNavigationViewVisibility(this)
        (activity as AppCompatActivity).supportActionBar?.hide()
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

    override val initEvent: ChatEvent
        get() = ChatEvent.Ui.LoadInitMessages(topicTitle, streamTitle)

    override fun createStore(): Store<ChatEvent, ChatEffect, ChatState> {
        return chatStoreFactory.provide()
    }

    override fun render(state: ChatState) {
        with(binding) {
            if (state.isLoading) {
                progressloader.visibility = View.VISIBLE
            }
            if (!state.isLoading) {
                progressloader.visibility = View.GONE
            }
            // если бд пустая и ответ с бэка еще не пришел - показываем loader
            if (state.messages.isEmpty() && state.messagesFromDb.isEmpty()) {
                progressloader.visibility = View.VISIBLE
            }
            if (state.messagesFromDb.isNotEmpty()) {
                Log.d("CHECKMESS2", "messagesFromDb")
                adapter.submitList(prepareDataForRecyclerView(state.messagesFromDb))
                binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
            if (state.messages.isNotEmpty()) {
                idLastMessage = state.messages[0].id
                sharedPreferences = requireActivity().getSharedPreferences(
                    ChannelsFragment.CHAT_SHARED_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.putString(ChannelsFragment.KEY_LAST_MESSAGE, idLastMessage.toString())
                    .apply()

                adapter.submitList(prepareDataForRecyclerView(state.messages))
                if (state.messages.size < 50) {
                    Log.d(
                        "CHECKMESS2",
                        "messages.size < 50: ${state.messages.size}, adapter.itemCount=${adapter.itemCount}"
                    )
                    binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                } else {
                    Log.d(
                        "CHECKMESS2",
                        "state.messages.size=${state.messages.size}, adapter.itemCount=${adapter.itemCount}"
                    )
                    binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 20)
                }


                // когда приходят актуальные данные с бэка, обновляем нашу бд
                // сохраняем в бд только последние 50 сообщений
                if (state.messagesCount == 50) {
                    // обновляем сообщения в бд
                    val eventToUpdateDb = ChatEvent.Ui.AddMessagesFromServerToDb(state.messages)
                    store.accept(eventToUpdateDb)
                }
            }
            state.error?.let {
                showSnackBarError("Ошибка: ${state.error.localizedMessage}")
            }
        }
    }

    private fun prepareDataForRecyclerView(messages: List<MessageModel>): List<DelegateItem> {
        dateList.clear()
        messageList.clear()
        // очищаем списки старых сообщений для загрузки новых
        messages.forEach {
            putMessageInOurChat(it)
        }
        return messageList.concatenateWithDate(dateList)
    }

    override fun handleEffect(effect: ChatEffect) = when (effect) {
        is ChatEffect.AddMessageReactionError -> {
            showSnackBarError(effect.errorMessage)
        }
    }

    companion object {

        const val RECEIVER_TYPE = "stream"
        const val EMPTY_EDIT_TEXT = ""
        const val MIN_MESSAGE_LENGTH = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
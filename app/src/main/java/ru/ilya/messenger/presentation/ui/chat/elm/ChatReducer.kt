package ru.ilya.messenger.presentation.ui.chat.elm

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import okhttp3.Credentials
import ru.ilya.messenger.di.App
import ru.ilya.messenger.presentation.ui.channels.ChannelsFragment
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatCommand
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatEffect
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatEvent
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatState
import ru.ilya.messenger.util.Constants
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChatReducer :
    DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    private lateinit var topicTitle: String
    private lateinit var streamTitle: String
    private var idLastMessage: String = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun Result.reduce(event: ChatEvent): Any = when (event) {
        is ChatEvent.Ui.LoadInitMessages -> {
            topicTitle = event.topicTitle
            streamTitle = event.streamTitle
            val narrowParam = listOf(
                hashMapOf("operator" to "topic", "operand" to topicTitle),
                hashMapOf("operator" to "stream", "operand" to streamTitle)
            )
            val json = Gson().toJson(narrowParam)
            // При старте экрана старый список сообщений очищается при открытии фрагмента/обновлении сообщений
            state { copy(isLoading = true, error = null, messagesCount = 50) }
            commands {
                +ChatCommand.LoadMessages(
                    auth = Credentials.basic(
                        Constants.EMAIL,
                        Constants.PASSWORD
                    ),
                    numBefore = Constants.END_MESSAGE_INDEX,
                    numAfter = Constants.START_MESSAGE_INDEX,
                    anchor = Constants.INIT_ANCHOR,
                    narrow = json
                )
            }
        }

        is ChatEvent.Ui.LoadNextMessages -> {
            topicTitle = event.topicTitle
            streamTitle = event.streamTitle

            val previousMessageId = idLastMessage
            // idLastMessage получим из shared preferences по конкретному стриму и топику

            sharedPreferences = App.instance.applicationContext.getSharedPreferences(
                ChannelsFragment.CHAT_SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            )
            idLastMessage = sharedPreferences
                .getString(
                    ChannelsFragment.KEY_LAST_MESSAGE,
                    ChannelsFragment.DEFAULT_STREAM_TITLE
                ) ?: ChannelsFragment.EMPTY_STREAM_TITLE
            Log.d("CHECKMESS", "previousMessageId=$previousMessageId, idLastMessage=$idLastMessage")
            if (previousMessageId != idLastMessage) {
                val narrowParam = listOf(
                    hashMapOf("operator" to "topic", "operand" to topicTitle),
                    hashMapOf("operator" to "stream", "operand" to streamTitle)
                )
                val json = Gson().toJson(narrowParam)
                state { copy(isLoading = true, error = null, messagesCount = 20) }
                commands {
                    +ChatCommand.LoadMessages(
                        auth = Credentials.basic(
                            Constants.EMAIL,
                            Constants.PASSWORD
                        ),
                        numBefore = Constants.NEXT_TWENTY_MESSAGES,
                        numAfter = Constants.START_MESSAGE_INDEX,
                        anchor = idLastMessage,
                        narrow = json
                    )
                }
            } else {
                // Значит это самое старое сообщение в чате
            }
        }

        is ChatEvent.Internal.UpdateMessages -> {
            // Эта команда аналогична ChatEvent.Ui.LoadInitMessages
            // Почему не вызываю LoadInitMessages вместо UpdateMessages во избежании
            // дублирования кода? Потому что UpdateMessages я вызываю из Internal, а не из Ui:
            // ChatEvent.Ui.LoadInitMessages / ChatEvent.Internal.UpdateMessages
            val narrowParam = listOf(
                hashMapOf("operator" to "topic", "operand" to topicTitle),
                hashMapOf("operator" to "stream", "operand" to streamTitle)
            )
            val json = Gson().toJson(narrowParam)
            // Отправляем команду на получение всего списка сообщений, после того как отправили мы
            state {
                copy(
                    isLoading = true,
                    error = null,
                    messages = emptyList(),
                    messagesCount = 20
                )
            }
            commands {
                +ChatCommand.LoadMessages(
                    auth = Credentials.basic(
                        Constants.EMAIL,
                        Constants.PASSWORD
                    ),
                    numBefore = Constants.END_MESSAGE_INDEX,
                    numAfter = Constants.START_MESSAGE_INDEX,
                    anchor = Constants.INIT_ANCHOR,
                    narrow = json
                )
            }
        }

        is ChatEvent.Internal.MessagesLoaded -> {
            when (state.messagesCount) {
                50 -> {
                    Log.d("CHECKPICTURE", "Пришли сообщения ${event.items}")
                    state {
                        copy(
                            isLoading = false,
                            error = null,
                            messagesFromDb = emptyList(),
                            messages = event.items
                        )
                    }
                }
                20 -> {
                    state {
                        copy(
                            isLoading = false,
                            error = null,
                            messagesFromDb = emptyList(),
                            messages = event.items + messages
                        )
                    }
                }
                else -> {
                    throw RuntimeException("Неизвестный messagesCount у ChatEvent.Internal.MessagesLoaded")
                }
            }
        }

        is ChatEvent.Ui.LoadMessagesFromDb -> {
            // Получаем список сообщений из БД по конкретному стриму
            commands { +ChatCommand.LoadMessagesFromDb(event.streamId) }
        }

        is ChatEvent.Internal.MessagesFromDbLoaded -> {
            // пришшел список сообщений из БД
            state {
                copy(
                    isLoading = false,
                    error = null,
                    messagesFromDb = event.messagesFromDb,
                    messages = emptyList()
                )
            }
        }

        is ChatEvent.Ui.AddMessagesFromServerToDb -> {
            commands { +ChatCommand.AddMessagesToDb(messagesToDb = event.messagesToDb) }
        }

        is ChatEvent.Ui.SendMessage -> {
            state { copy(isLoading = true, error = null, messages = emptyList()) }
            commands {
                +ChatCommand.SendMessage(
                    auth = Credentials.basic(
                        Constants.EMAIL,
                        Constants.PASSWORD
                    ),
                    type = event.type,
                    content = event.content,
                    to = event.to,
                    topic = event.topic
                )
            }
        }
        is ChatEvent.Internal.Loading -> {}

        is ChatEvent.Internal.ErrorLoading -> {
            when (event.error.message) {
                "HTTP 400 " -> {
                    effects { +ChatEffect.AddMessageReactionError("Такой эмодзи уже есть!") }
                    state { copy(isLoading = false) }
                }
                else -> {}
            }
        }

        is ChatEvent.Ui.AddMessageReaction -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChatCommand.AddMessageReaction(
                    auth = event.auth,
                    messageId = event.messageId,
                    emojiName = event.emojiName
                )
            }
        }
        is ChatEvent.Ui.DeleteMessageReaction -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChatCommand.DeleteMessageReaction(
                    auth = event.auth,
                    messageId = event.messageId,
                    emojiName = event.emojiName
                )
            }
        }
    }


}

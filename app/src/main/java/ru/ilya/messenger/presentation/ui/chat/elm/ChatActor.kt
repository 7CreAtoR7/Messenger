package ru.ilya.messenger.presentation.ui.chat.elm


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.ilya.messenger.domain.entities.Result
import ru.ilya.messenger.domain.useCases.AddMessageReactionUseCase
import ru.ilya.messenger.domain.useCases.DeleteMessageReactionUseCase
import ru.ilya.messenger.domain.useCases.GetTopicMessagesUseCase
import ru.ilya.messenger.domain.useCases.SendMessageToTopicUseCase
import ru.ilya.messenger.domain.useCases.dbUseCases.AddMessagesInDbUseCase
import ru.ilya.messenger.domain.useCases.dbUseCases.GetMessagesFromDbUseCase
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatCommand
import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatEvent
import vivid.money.elmslie.core.disposable.Disposable
import vivid.money.elmslie.core.store.DefaultActor
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class ChatActor @Inject constructor(
    private val getTopicMessagesUseCase: GetTopicMessagesUseCase,
    private val sendMessageToTopicUseCase: SendMessageToTopicUseCase,
    private val addMessageReactionUseCase: AddMessageReactionUseCase,
    private val deleteMessageReactionUseCase: DeleteMessageReactionUseCase,
    private val getMessagesFromDbUseCase: GetMessagesFromDbUseCase,
    private val addMessagesInDbUseCase: AddMessagesInDbUseCase
) : Actor<ChatCommand, ChatEvent>, DefaultActor<Any, Any> {

    override fun execute(command: ChatCommand): Flow<ChatEvent> = when (command) {
        is ChatCommand.LoadMessages -> {
            getTopicMessagesUseCase.invoke(
                auth = command.auth,
                numBefore = command.numBefore,
                numAfter = command.numAfter,
                anchor = command.anchor,
                narrow = command.narrow
            )
                .map { result ->
                    when (result) {
                        is Result.Success -> {
                            ChatEvent.Internal.MessagesLoaded(result.data)
                        }
                        is Result.Loading -> {
                            ChatEvent.Internal.Loading
                        }
                        is Result.Error -> {
                            ChatEvent.Internal.ErrorLoading(Exception(result.message))
                        }
                    }
                }
                .catch { e ->
                    emit(ChatEvent.Internal.ErrorLoading(e))
                }
                .flowOn(Dispatchers.IO)
        }
        is ChatCommand.SendMessage -> {
            sendMessageToTopicUseCase.invoke(
                auth = command.auth,
                type = command.type,
                content = command.content,
                to = command.to,
                topic = command.topic
            ).map { result ->
                when (result) {
                    is Result.Success -> {
                        // сообщение успешно отправлено - запрашиваем новый список сообщений
                        ChatEvent.Internal.UpdateMessages
                    }
                    is Result.Error -> {
                        ChatEvent.Internal.ErrorLoading(Exception(result.message))
                    }
                    is Result.Loading -> {
                        ChatEvent.Internal.Loading
                    }
                }
            }
        }

        is ChatCommand.LoadMessagesFromDb -> {
            flow<ChatEvent> {
                val listMessageModel = getMessagesFromDbUseCase.invoke(command.streamId)
                emit(ChatEvent.Internal.MessagesFromDbLoaded(listMessageModel))
            }.catch { e ->
                emit(ChatEvent.Internal.ErrorLoading(e))
            }.flowOn(Dispatchers.IO)
        }

        is ChatCommand.AddMessagesToDb -> {
            flow<ChatEvent> {
                addMessagesInDbUseCase.invoke(command.messagesToDb)
            }.catch {
            }.flowOn(Dispatchers.IO)
        }

        is ChatCommand.AddMessageReaction -> {
            addMessageReactionUseCase.invoke(
                auth = command.auth,
                messageId = command.messageId,
                emojiName = command.emojiName
            ).map { result ->
                when (result) {
                    is Result.Success -> {
                        // эмодзи добавлен - обновляем список сообщений
                        ChatEvent.Internal.UpdateMessages
                    }
                    is Result.Error -> {
                        ChatEvent.Internal.ErrorLoading(Exception(result.message))
                    }
                    is Result.Loading -> {
                        ChatEvent.Internal.Loading
                    }
                }
            }
        }
        is ChatCommand.DeleteMessageReaction -> {
            deleteMessageReactionUseCase.invoke(
                auth = command.auth,
                messageId = command.messageId,
                emojiName = command.emojiName
            ).map { result ->
                when (result) {
                    is Result.Success -> {
                        // удален добавлен - обновляем список сообщений
                        ChatEvent.Internal.UpdateMessages
                    }
                    is Result.Error -> {
                        ChatEvent.Internal.ErrorLoading(Exception(result.message))
                    }
                    is Result.Loading -> {
                        ChatEvent.Internal.Loading
                    }
                }
            }
        }
    }

    override fun execute(
        command: Any,
        onEvent: (Any) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable {
        TODO("Not yet implemented")
    }

}
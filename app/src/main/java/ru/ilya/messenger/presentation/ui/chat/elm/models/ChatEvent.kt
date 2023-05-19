package ru.ilya.messenger.presentation.ui.chat.elm.models

import ru.ilya.messenger.domain.entities.MessageModel

sealed class ChatEvent {

    sealed class Ui : ChatEvent() {

        data class LoadMessagesFromDb(val streamId: Int) : Ui()

        data class AddMessagesFromServerToDb(val messagesToDb: List<MessageModel>) : Ui()

        data class LoadNextMessages(val topicTitle: String, val streamTitle: String) : Ui()

        data class LoadInitMessages(val topicTitle: String, val streamTitle: String) : Ui()

        data class SendMessage(
            val auth: String,
            val type: String,
            val content: String,
            val to: String,
            val topic: String
        ) : Ui()

        data class AddMessageReaction(
            val auth: String,
            val messageId: Int,
            val emojiName: String
        ) : Ui()

        data class DeleteMessageReaction(
            val auth: String,
            val messageId: Int,
            val emojiName: String
        ) : Ui()
    }

    // результат команды загрузки сообщений - сам список либо ошибка
    sealed class Internal : ChatEvent() {

        data class MessagesLoaded(val items: List<MessageModel>) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()

        object Loading : Internal()

        object UpdateMessages : Internal()

        data class MessagesFromDbLoaded(val messagesFromDb: List<MessageModel>) : Internal()

    }
}
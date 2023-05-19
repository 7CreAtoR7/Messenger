package ru.ilya.messenger.presentation.ui.chat.elm.models

import ru.ilya.messenger.domain.entities.MessageModel

// асинхр операция загрузки чата
sealed class ChatCommand {

    data class LoadMessages(
        val auth: String,
        val numBefore: Int,
        val numAfter: Int,
        val anchor: String,
        val narrow: String
    ) : ChatCommand()

    data class LoadMessagesFromDb(val streamId: Int) : ChatCommand()

    data class AddMessagesToDb(val messagesToDb: List<MessageModel>) : ChatCommand()

    data class SendMessage(
        val auth: String,
        val type: String,
        val content: String,
        val to: String,
        val topic: String
    ) : ChatCommand()

    data class AddMessageReaction(
        val auth: String,
        val messageId: Int,
        val emojiName: String
    ) : ChatCommand()

    data class DeleteMessageReaction(
        val auth: String,
        val messageId: Int,
        val emojiName: String
    ) : ChatCommand()

}

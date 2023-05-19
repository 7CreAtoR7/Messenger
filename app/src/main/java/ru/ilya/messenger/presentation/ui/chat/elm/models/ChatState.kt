package ru.ilya.messenger.presentation.ui.chat.elm.models

import ru.ilya.messenger.domain.entities.MessageModel

// на экране people 2 состояния:
// загрузка и отображение загруженного списка сообщений
data class ChatState(
    val isLoading: Boolean = false,
    val messages: List<MessageModel> = emptyList(),
    val messagesFromDb: List<MessageModel> = emptyList(),
    val error: Throwable? = null,
    val userStatus: Boolean = false,
    val messagesCount: Int = INIT_MESSAGES_TO_LOAD
) {
    private companion object {
        const val INIT_MESSAGES_TO_LOAD = 50
    }
}

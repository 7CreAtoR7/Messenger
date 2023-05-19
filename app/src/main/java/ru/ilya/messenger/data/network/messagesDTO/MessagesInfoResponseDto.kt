package ru.ilya.messenger.data.network.messagesDTO

data class MessagesInfoResponseDto(
    val result: String? = null,
    val messages: List<MessageResponseDto> = emptyList()
)

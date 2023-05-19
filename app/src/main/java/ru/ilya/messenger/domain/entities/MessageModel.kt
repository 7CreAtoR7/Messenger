package ru.ilya.messenger.domain.entities

data class MessageModel(

    var id: Int,
    var avatarUrl: String,
    var content: String,
    var reactions: List<Reactions> = emptyList(),
    var senderEmail: String,
    var senderFullName: String,
    var senderId: Int,
    var streamId: Int,
    var timestamp: Long
)
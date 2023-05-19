package ru.ilya.messenger.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ilya.messenger.data.network.messagesDTO.AddReactionResponseDto
import ru.ilya.messenger.data.network.messagesDTO.DeleteReactionResponseDto
import ru.ilya.messenger.domain.entities.*
import ru.ilya.messenger.domain.entities.Result


interface MessengerRepository {

    suspend fun addMessagesInDb(listMessages: List<MessageModel>)

    suspend fun getMessagesFromDb(streamId: Int): List<MessageModel>

    suspend fun getAllStreamsWithTopicsFromDb(isSubscribed: Boolean): List<StreamsWithTopicsEntityModel>

    suspend fun addAllStreamWithTopicsInDb(listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>)

    suspend fun getSubscribedStreamsWithTopicsFromDb(isSubscribed: Boolean): List<StreamsWithTopicsEntityModel>

    suspend fun addSubscribedStreamWithTopicsInDb(listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>)

    fun loadUsers(auth: String): Flow<Result<List<UserPeople>>>

    suspend fun loadUserStatus(auth: String, userId: Int): Result<AggregatedModel>

    fun getSubscribedStreams(auth: String): Flow<Result<List<StreamData>>>

    fun getAllStreams(auth: String): Flow<Result<List<StreamAllModel>>>

    suspend fun getTopicsInStreamById(auth: String, streamId: Int): Result<List<TopicData>>

    fun getTopicMessages(
        auth: String,
        numBefore: Int,
        numAfter: Int,
        anchor: String,
        narrow: String
    ): Flow<Result<List<MessageModel>>>

    fun sendMessageInTopic(
        auth: String,
        type: String,
        content: String,
        to: String,
        topic: String,
    ): Flow<Result<SendMessageResponseModel>>

    fun addMessageReaction(
        auth: String,
        messageId: Int,
        emojiName: String
    ): Flow<Result<AddReactionResponseDto>>

    fun deleteMessageReaction(
        auth: String,
        messageId: Int,
        emojiName: String,
    ): Flow<Result<DeleteReactionResponseDto>>

}
package ru.ilya.messenger.data.network

import retrofit2.http.*
import ru.ilya.messenger.data.network.messagesDTO.AddReactionResponseDto
import ru.ilya.messenger.data.network.messagesDTO.DeleteReactionResponseDto
import ru.ilya.messenger.data.network.messagesDTO.MessagesInfoResponseDto
import ru.ilya.messenger.data.network.messagesDTO.SendMessageResponseDto
import ru.ilya.messenger.data.network.streamsDTO.AllStreamsResponseDTO
import ru.ilya.messenger.data.network.streamsDTO.SubscribedStreamsResponseDTO
import ru.ilya.messenger.data.network.topicsDTO.AllTopicsResponseDTO
import ru.ilya.messenger.data.network.usersDTO.AllMembersResponseDto
import ru.ilya.messenger.data.network.usersDTO.userStatusDTO.UserStatusResponseDto

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("users")
    suspend fun loadUsers(
        @Header("Authorization") auth: String
    ): AllMembersResponseDto

    @Headers("Content-Type: application/json")
    @GET("users/{user_id_or_email}/presence")
    suspend fun loadUsersStatus(
        @Header("Authorization") auth: String,
        @Path("user_id_or_email") userId: Int
    ): UserStatusResponseDto

    @Headers("Content-Type: application/json")
    @GET("streams")
    suspend fun getAllStreams(
        @Header("Authorization") auth: String
    ): AllStreamsResponseDTO

    @Headers("Content-Type: application/json")
    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(
        @Header("Authorization") auth: String
    ): SubscribedStreamsResponseDTO

    @Headers("Content-Type: application/json")
    @GET("users/me/{streamId}/topics")
    suspend fun getTopicsInStreamById(
        @Header("Authorization") auth: String,
        @Path("streamId") streamId: Int
    ): AllTopicsResponseDTO

    @Headers("Content-Type: application/json")
    @POST("messages")
    suspend fun sendMessageInTopic(
        @Header("Authorization") auth: String,
        @Query("type") type: String,
        @Query("content") content: String,
        @Query("to") to: String,
        @Query("topic") topic: String,
    ): SendMessageResponseDto

    @Headers("Content-Type: application/json")
    @GET("messages")
    suspend fun getTopicMessages(
        @Header("Authorization") auth: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("anchor") anchor: String,
        @Query("narrow") narrow: String,
        @Query("include_anchor") includeAnchor: Boolean,
    ): MessagesInfoResponseDto

    @Headers("Content-Type: application/json")
    @POST("messages/{message_id}/reactions")
    suspend fun addMessageReaction(
        @Header("Authorization") auth: String,
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): AddReactionResponseDto

    @Headers("Content-Type: application/json")
    @DELETE("messages/{message_id}/reactions")
    suspend fun deleteMessageReaction(
        @Header("Authorization") auth: String,
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): DeleteReactionResponseDto

}
package ru.ilya.messenger.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.ilya.messenger.data.database.dao.AllStreamsDao
import ru.ilya.messenger.data.database.dao.MessagesDao
import ru.ilya.messenger.data.database.dao.SubscribedStreamsDao
import ru.ilya.messenger.data.mapper.AppMapper
import ru.ilya.messenger.data.network.ApiService
import ru.ilya.messenger.data.network.messagesDTO.AddReactionResponseDto
import ru.ilya.messenger.data.network.messagesDTO.DeleteReactionResponseDto
import ru.ilya.messenger.domain.entities.*
import ru.ilya.messenger.domain.repository.MessengerRepository
import ru.ilya.messenger.util.Constants
import javax.inject.Inject

class MessengerRepositoryImpl @Inject constructor(
    private val subscribedStreamsDao: SubscribedStreamsDao,
    private val allStreamsDao: AllStreamsDao,
    private val messagesDao: MessagesDao,
    private val mapper: AppMapper,
    private val api: ApiService
) : MessengerRepository {


    override suspend fun getSubscribedStreamsWithTopicsFromDb(isSubscribed: Boolean): List<StreamsWithTopicsEntityModel> {
        val listStreamsDbModel =
            subscribedStreamsDao.getSubscribedStreamsWithTopicsFromDb(isSubscribed = isSubscribed)
        return mapper.mapListSubscribedStreamsWithTopicsDbModelToListSubscribedStreamsWithTopicsEntityModel(
            listStreamsDbModel
        )
    }

    override suspend fun addSubscribedStreamWithTopicsInDb(listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>) {
        subscribedStreamsDao.addSubscribedStreamWithTopicsInDb(
            mapper.mapListStreamsWithTopicsEntityModelToListSubscribedStreamsWithTopicsDbModel(
                listStreamsWithTopicsEntityModel
            )
        )
    }

    override suspend fun addMessagesInDb(listMessages: List<MessageModel>) {
        messagesDao.addMessagesInDb(mapper.mapListMessageModelToListMessageDbModel(listMessages))
    }

    override suspend fun getMessagesFromDb(streamId: Int): List<MessageModel> {

        return mapper.mapListMessageDbModelToListMessageModel(messagesDao.getMessagesFromDb(streamId))
    }

    override suspend fun getAllStreamsWithTopicsFromDb(isSubscribed: Boolean): List<StreamsWithTopicsEntityModel> {
        val listStreamsDbModel =
            allStreamsDao.getAllStreamsWithTopicsFromDb(isSubscribed = isSubscribed)
        return mapper.mapListAllStreamsWithTopicsDbModelToListAllStreamsWithTopicsEntityModel(
            listStreamsDbModel
        )
    }

    override suspend fun addAllStreamWithTopicsInDb(listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>) {
        allStreamsDao.addAllStreamWithTopicsInDb(
            mapper.mapListStreamsWithTopicsEntityModelToListAllStreamsWithTopicsDbModel(
                listStreamsWithTopicsEntityModel
            )
        )
    }

    override fun loadUsers(auth: String): Flow<Result<List<UserPeople>>> {
        return flow {
            try {
                val response = api.loadUsers(auth)
                if (response.result == Constants.STATUS_SUCCESS) {
                    val members = response.members.map { memberDto ->
                        mapper.mapMemberResponseDtoToUserPeople(memberDto)
                    }
                    emit(Result.Success(members))
                } else {
                    emit(Result.Error(response.msg ?: Constants.UNEXPECTED_ERROR))
                }
            } catch (e: Exception) {
                emit(Result.Error(Constants.NETWORK_ERROR))
            }
        }
    }

    override suspend fun loadUserStatus(
        auth: String,
        userId: Int
    ): Result<AggregatedModel> {

        return try {
            val response = api.loadUsersStatus(auth = auth, userId = userId)
            if (response.result == Constants.STATUS_SUCCESS) {
                val memberStatus =
                    mapper.mapAggregatedDtoToPresenceModel(response.presence.aggregated)
                Result.Success(memberStatus)
            } else {
                Result.Error(response.msg ?: Constants.UNEXPECTED_ERROR)
            }
        } catch (e: java.lang.Exception) {
            Result.Error(Constants.NETWORK_ERROR)
        }

    }

    override fun getAllStreams(auth: String): Flow<Result<List<StreamAllModel>>> {
        return flow {

            try {
                val response = api.getAllStreams(auth = auth)
                if (response.result == Constants.STATUS_SUCCESS) {
                    val allStreams =
                        mapper.mapListAllStreamDtoToListStreamAllModel(response.streams)
                    emit(Result.Success(allStreams))
                } else {
                    emit(Result.Error(response.result ?: Constants.UNEXPECTED_ERROR))
                }
            } catch (e: Exception) {
                emit(Result.Error(Constants.NETWORK_ERROR))
            }
        }
    }

    override fun getSubscribedStreams(auth: String): Flow<Result<List<StreamData>>> {
        return flow {

            try {
                val response = api.getSubscribedStreams(auth = auth)
                if (response.result == Constants.STATUS_SUCCESS) {
                    val subscribedStreams =
                        mapper.mapListSubscribedStreamResponseDtoToListStreamData(response.subscriptions)
                    emit(Result.Success(subscribedStreams))
                } else {
                    emit(Result.Error(response.result ?: Constants.UNEXPECTED_ERROR))
                }
            } catch (e: Exception) {
                emit(Result.Error(Constants.NETWORK_ERROR))
            }
        }
    }

    override suspend fun getTopicsInStreamById(
        auth: String,
        streamId: Int
    ): Result<List<TopicData>> {

        return try {
            val response = api.getTopicsInStreamById(auth = auth, streamId = streamId)
            if (response.result == Constants.STATUS_SUCCESS) {
                val topics = mapper.mapListTopicResponseDtoToListTopicData(response.topics)
                Result.Success(topics)
            } else {
                Result.Error(response.result ?: Constants.UNEXPECTED_ERROR)
            }
        } catch (e: java.lang.Exception) {
            Result.Error(Constants.NETWORK_ERROR)
        }

    }

    override fun getTopicMessages(
        auth: String,
        numBefore: Int,
        numAfter: Int,
        anchor: String,
        narrow: String
    ): Flow<Result<List<MessageModel>>> {
        return flow {

            try {
                val response = api.getTopicMessages(
                    auth = auth,
                    numBefore = numBefore,
                    numAfter = numAfter,
                    anchor = anchor,
                    narrow = narrow,
                    includeAnchor = false
                )
                if (response.result == Constants.STATUS_SUCCESS) {
                    val messages =
                        mapper.mapListMessageResponseDtoToListMessageModel(response.messages)
                    // add to db
                    emit(Result.Success(messages))
                } else {
                    emit(Result.Error(response.result ?: Constants.UNEXPECTED_ERROR))
                }
            } catch (e: Exception) {
                emit(Result.Error(Constants.NETWORK_ERROR))
            }
        }
    }

    override fun sendMessageInTopic(
        auth: String,
        type: String,
        content: String,
        to: String,
        topic: String
    ): Flow<Result<SendMessageResponseModel>> {
        return flow {

            try {
                val response = api.sendMessageInTopic(
                    auth = auth,
                    type = type,
                    content = content,
                    to = to,
                    topic = topic
                )
                if (response.result == Constants.STATUS_SUCCESS) {
                    val resultOfSending =
                        mapper.mapSendMessageResponseDtoToSendMessageResponseModel(response)
                    emit(Result.Success(resultOfSending))
                } else {
                    emit(Result.Error(response.result ?: Constants.UNEXPECTED_ERROR))
                }
            } catch (e: Exception) {
                emit(Result.Error(Constants.NETWORK_ERROR))
            }
        }
    }

    override fun addMessageReaction(
        auth: String,
        messageId: Int,
        emojiName: String
    ): Flow<Result<AddReactionResponseDto>> {
        return flow {
            try {
                val response = api.addMessageReaction(
                    auth = auth,
                    messageId = messageId,
                    emojiName = emojiName
                )
                if (response.result == Constants.STATUS_SUCCESS) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.result ?: Constants.UNEXPECTED_ERROR))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message!!))
            }
        }
    }

    override fun deleteMessageReaction(
        auth: String,
        messageId: Int,
        emojiName: String
    ): Flow<Result<DeleteReactionResponseDto>> {
        return flow {

            try {
                val response = api.deleteMessageReaction(
                    auth = auth,
                    messageId = messageId,
                    emojiName = emojiName
                )
                if (response.result == Constants.STATUS_SUCCESS) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.result ?: Constants.UNEXPECTED_ERROR))
                }
            } catch (e: Exception) {
                emit(Result.Error(Constants.NETWORK_ERROR))
            }
        }
    }
}
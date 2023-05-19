package ru.ilya.messenger.data.mapper

import ru.ilya.messenger.data.database.entity.AllStreamsWithTopicsDbModel
import ru.ilya.messenger.data.database.entity.MessageDbModel
import ru.ilya.messenger.data.database.entity.SubscribedStreamsWithTopicsDbModel
import ru.ilya.messenger.data.network.messagesDTO.MessageResponseDto
import ru.ilya.messenger.data.network.messagesDTO.SendMessageResponseDto
import ru.ilya.messenger.data.network.streamsDTO.AllStreamDto
import ru.ilya.messenger.data.network.streamsDTO.SubscribedStreamDto
import ru.ilya.messenger.data.network.topicsDTO.TopicResponseDto
import ru.ilya.messenger.data.network.usersDTO.UserResponseDto
import ru.ilya.messenger.data.network.usersDTO.userStatusDTO.AggregatedDto
import ru.ilya.messenger.domain.entities.*
import javax.inject.Inject

class AppMapper @Inject constructor() {

    private fun mapSubscribedStreamsWithTopicsDbModelToStreamsWithTopicsEntityModel(
        subscribedStreamsWithTopicsDbModel: SubscribedStreamsWithTopicsDbModel
    ): StreamsWithTopicsEntityModel {
        return StreamsWithTopicsEntityModel(
            id = subscribedStreamsWithTopicsDbModel.id,
            streamId = subscribedStreamsWithTopicsDbModel.streamId,
            streamName = subscribedStreamsWithTopicsDbModel.streamName,
            isSubscribed = subscribedStreamsWithTopicsDbModel.isSubscribed,
            topicName = subscribedStreamsWithTopicsDbModel.topicName
        )
    }

    fun mapListSubscribedStreamsWithTopicsDbModelToListSubscribedStreamsWithTopicsEntityModel(
        listSubscribedStreamsWithTopicsDbModel: List<SubscribedStreamsWithTopicsDbModel>
    ): List<StreamsWithTopicsEntityModel> {
        return listSubscribedStreamsWithTopicsDbModel.map {
            mapSubscribedStreamsWithTopicsDbModelToStreamsWithTopicsEntityModel(
                it
            )
        }
    }

    private fun mapAllStreamsWithTopicsDbModelToStreamsWithTopicsEntityModel(
        allStreamsWithTopicsDbModel: AllStreamsWithTopicsDbModel
    ): StreamsWithTopicsEntityModel {
        return StreamsWithTopicsEntityModel(
            id = allStreamsWithTopicsDbModel.id,
            streamId = allStreamsWithTopicsDbModel.streamId,
            streamName = allStreamsWithTopicsDbModel.streamName,
            isSubscribed = allStreamsWithTopicsDbModel.isSubscribed,
            topicName = allStreamsWithTopicsDbModel.topicName
        )
    }

    fun mapListAllStreamsWithTopicsDbModelToListAllStreamsWithTopicsEntityModel(
        listAllStreamsWithTopicsDbModel: List<AllStreamsWithTopicsDbModel>
    ): List<StreamsWithTopicsEntityModel> {
        return listAllStreamsWithTopicsDbModel.map {
            mapAllStreamsWithTopicsDbModelToStreamsWithTopicsEntityModel(
                it
            )
        }
    }

    private fun mapStreamsWithTopicsEntityModelToAllStreamsWithTopicsDbModel(
        streamsWithTopicsEntityModel: StreamsWithTopicsEntityModel
    ): AllStreamsWithTopicsDbModel {
        return AllStreamsWithTopicsDbModel(
            id = streamsWithTopicsEntityModel.id,
            streamId = streamsWithTopicsEntityModel.streamId,
            streamName = streamsWithTopicsEntityModel.streamName,
            isSubscribed = streamsWithTopicsEntityModel.isSubscribed,
            topicName = streamsWithTopicsEntityModel.topicName
        )
    }

    fun mapListStreamsWithTopicsEntityModelToListAllStreamsWithTopicsDbModel(
        listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>
    ): List<AllStreamsWithTopicsDbModel> {
        return listStreamsWithTopicsEntityModel.map {
            mapStreamsWithTopicsEntityModelToAllStreamsWithTopicsDbModel(
                it
            )
        }
    }

    private fun mapStreamsWithTopicsEntityModelToStreamsWithTopicsDbModel(
        streamsWithTopicsEntityModel: StreamsWithTopicsEntityModel
    ): SubscribedStreamsWithTopicsDbModel {
        return SubscribedStreamsWithTopicsDbModel(
            id = streamsWithTopicsEntityModel.id,
            streamId = streamsWithTopicsEntityModel.streamId,
            streamName = streamsWithTopicsEntityModel.streamName,
            isSubscribed = streamsWithTopicsEntityModel.isSubscribed,
            topicName = streamsWithTopicsEntityModel.topicName
        )
    }

    fun mapListStreamsWithTopicsEntityModelToListSubscribedStreamsWithTopicsDbModel(
        listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>
    ): List<SubscribedStreamsWithTopicsDbModel> {
        return listStreamsWithTopicsEntityModel.map {
            mapStreamsWithTopicsEntityModelToStreamsWithTopicsDbModel(
                it
            )
        }
    }

    private fun mapMessageModelToMessageDbModel(messageModel: MessageModel): MessageDbModel {
        return MessageDbModel(
            id = messageModel.id,
            avatarUrl = messageModel.avatarUrl,
            content = messageModel.content,
            reactions = messageModel.reactions,
            senderEmail = messageModel.senderEmail,
            senderFullName = messageModel.senderFullName,
            senderId = messageModel.senderId,
            streamId = messageModel.streamId,
            timestamp = messageModel.timestamp
        )
    }

    fun mapListMessageModelToListMessageDbModel(listMessageModel: List<MessageModel>): List<MessageDbModel> {
        return listMessageModel.map {
            mapMessageModelToMessageDbModel(it)
        }
    }

    private fun mapMessageDbModelToMessageModel(messageDbModel: MessageDbModel): MessageModel {
        return MessageModel(
            id = messageDbModel.id,
            avatarUrl = messageDbModel.avatarUrl,
            content = messageDbModel.content,
            reactions = messageDbModel.reactions,
            senderEmail = messageDbModel.senderEmail,
            senderFullName = messageDbModel.senderFullName,
            senderId = messageDbModel.senderId,
            streamId = messageDbModel.streamId,
            timestamp = messageDbModel.timestamp
        )
    }

    fun mapListMessageDbModelToListMessageModel(listMessageDbModel: List<MessageDbModel>): List<MessageModel> {
        return listMessageDbModel.map {
            mapMessageDbModelToMessageModel(it)
        }
    }

    private fun mapMessageResponseDtoToMessageModel(messageResponseDto: MessageResponseDto): MessageModel {
        val regex = Regex("<[/]?p>") // from <p>messageText</p> to messageText
        val contentWithoutHtmlTags = messageResponseDto.content?.replace(regex, "")
        return MessageModel(
            id = messageResponseDto.id ?: DEFAULT_ID,
            avatarUrl = messageResponseDto.avatarUrl ?: EMPTY_LINE,
            content = contentWithoutHtmlTags ?: EMPTY_LINE,
            reactions = messageResponseDto.reactions,
            senderEmail = messageResponseDto.senderEmail ?: EMPTY_LINE,
            senderFullName = messageResponseDto.senderFullName ?: EMPTY_LINE,
            senderId = messageResponseDto.senderId ?: DEFAULT_ID,
            streamId = messageResponseDto.streamId ?: DEFAULT_ID,
            timestamp = messageResponseDto.timestamp ?: DEFAULT_TIMESTAMP,
        )
    }

    fun mapListMessageResponseDtoToListMessageModel(list: List<MessageResponseDto>): List<MessageModel> {
        return list.map {
            mapMessageResponseDtoToMessageModel(it)
        }
    }

    private fun mapStreamResponseDtoToStreamData(subscribedStreamDto: SubscribedStreamDto): StreamData {
        return StreamData(
            streamId = subscribedStreamDto.streamId ?: DEFAULT_ID,
            name = subscribedStreamDto.name ?: EMPTY_LINE,
        )
    }

    fun mapListSubscribedStreamResponseDtoToListStreamData(list: List<SubscribedStreamDto>): List<StreamData> {
        return list.map {
            mapStreamResponseDtoToStreamData(it)
        }
    }

    fun mapMemberResponseDtoToUserPeople(userResponseDto: UserResponseDto): UserPeople {
        return UserPeople(
            email = userResponseDto.email ?: EMPTY_LINE,
            userId = userResponseDto.userId ?: DEFAULT_ID,
            fullName = userResponseDto.fullName ?: EMPTY_LINE,
            timezone = userResponseDto.timezone ?: EMPTY_LINE,
            isActive = userResponseDto.isActive ?: FALSE,
            dateJoined = userResponseDto.dateJoined ?: EMPTY_LINE,
            avatarUrl = userResponseDto.avatarUrl ?: EMPTY_LINE,
            deliveryEmail = userResponseDto.deliveryEmail ?: EMPTY_LINE,
            status = EMPTY_LINE
        )
    }

    private fun mapTopicResponseDtoToTopicData(topicResponseDto: TopicResponseDto): TopicData {
        return TopicData(
            name = topicResponseDto.name ?: EMPTY_LINE
        )
    }

    fun mapListTopicResponseDtoToListTopicData(list: List<TopicResponseDto>): List<TopicData> {
        return list.map {
            mapTopicResponseDtoToTopicData(it)
        }
    }

    fun mapAggregatedDtoToPresenceModel(aggregatedDto: AggregatedDto): AggregatedModel {
        return AggregatedModel(
            status = aggregatedDto.status ?: EMPTY_LINE,
            timestamp = aggregatedDto.timestamp ?: DEFAULT_INT
        )
    }

    fun mapSendMessageResponseDtoToSendMessageResponseModel(
        sendMessageResponseDto: SendMessageResponseDto
    ): SendMessageResponseModel {
        return SendMessageResponseModel(
            id = sendMessageResponseDto.id ?: DEFAULT_ID,
            result = sendMessageResponseDto.result ?: EMPTY_LINE
        )
    }

    private fun mapAllStreamDtoToStreamAllModel(allStreamDto: AllStreamDto): StreamAllModel {
        return StreamAllModel(
            streamId = allStreamDto.streamId ?: DEFAULT_ID,
            name = allStreamDto.name ?: EMPTY_LINE,
        )
    }

    fun mapListAllStreamDtoToListStreamAllModel(list: List<AllStreamDto>): List<StreamAllModel> {
        return list.map {
            mapAllStreamDtoToStreamAllModel(it)
        }
    }

    companion object {

        const val DEFAULT_ID = 0
        const val DEFAULT_INT = 0
        const val DEFAULT_TIMESTAMP = 0L
        const val EMPTY_LINE = ""
        const val FALSE = false
    }

}
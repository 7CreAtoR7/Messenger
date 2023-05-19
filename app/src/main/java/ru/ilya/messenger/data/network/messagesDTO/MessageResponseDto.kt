package ru.ilya.messenger.data.network.messagesDTO

import com.google.gson.annotations.SerializedName
import ru.ilya.messenger.domain.entities.Reactions

data class MessageResponseDto(

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @SerializedName("client")
    var client: String? = null,

    @SerializedName("content")
    var content: String? = null,

    @SerializedName("content_type")
    var contentType: String? = null,

    @SerializedName("display_recipient")
    var displayRecipient: String? = null,

    @SerializedName("is_me_message")
    var isMeMessage: Boolean? = null,

    @SerializedName("reactions")
    var reactions: List<Reactions> = emptyList(),

    @SerializedName("recipient_id")
    var recipientId: Int? = null,

    @SerializedName("sender_email")
    var senderEmail: String? = null,

    @SerializedName("sender_full_name")
    var senderFullName: String? = null,

    @SerializedName("sender_id")
    var senderId: Int? = null,

    @SerializedName("sender_realm_str")
    var senderRealmStr: String? = null,

    @SerializedName("stream_id")
    var streamId: Int? = null,

    @SerializedName("subject")
    var subject: String? = null,

    @SerializedName("submessages")
    var submessages: List<String> = emptyList(),

    @SerializedName("timestamp")
    var timestamp: Long? = null,

    @SerializedName("topic_links")
    var topicLinks: List<String> = emptyList(),

    @SerializedName("flags")
    var flags: List<String> = emptyList(),

    @SerializedName("type")
    var type: String? = null
)
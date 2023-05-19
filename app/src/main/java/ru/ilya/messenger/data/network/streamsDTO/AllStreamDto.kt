package ru.ilya.messenger.data.network.streamsDTO

import com.google.gson.annotations.SerializedName

data class AllStreamDto(
    @SerializedName("can_remove_subscribers_group_id")
    var canRemoveSubscribersGroupId: Int? = null,

    @SerializedName("date_created")
    var dateCreated: Int? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("first_message_id")
    var firstMessageId: Int? = null,

    @SerializedName("history_public_to_subscribers")
    var historyPublicToSubscribers: Boolean? = null,

    @SerializedName("invite_only")
    var inviteOnly: Boolean? = null,

    @SerializedName("is_web_public")
    var isWebPublic: Boolean? = null,

    @SerializedName("message_retention_days")
    var messageRetentionDays: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("rendered_description")
    var renderedDescription: String? = null,

    @SerializedName("stream_id")
    var streamId: Int? = null,

    @SerializedName("stream_post_policy")
    var streamPostPolicy: Int? = null,

    @SerializedName("is_announcement_only")
    var isAnnouncementOnly: Boolean? = null

)
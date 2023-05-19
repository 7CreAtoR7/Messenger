package ru.ilya.messenger.data.network.streamsDTO

import com.google.gson.annotations.SerializedName

data class SubscribedStreamDto(

    @SerializedName("stream_id")
    val streamId: Int? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("date_created")
    val dateCreated: Int? = null
)
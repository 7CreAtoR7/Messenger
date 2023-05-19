package ru.ilya.messenger.data.network.topicsDTO

import com.google.gson.annotations.SerializedName

data class TopicResponseDto(

    @SerializedName("max_id")
    val maxId: Int? = null,

    @SerializedName("name")
    val name: String? = null
)
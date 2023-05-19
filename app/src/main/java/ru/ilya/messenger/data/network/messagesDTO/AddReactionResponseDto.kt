package ru.ilya.messenger.data.network.messagesDTO

import com.google.gson.annotations.SerializedName

data class AddReactionResponseDto(

    @SerializedName("result")
    var result: String? = null,

    @SerializedName("msg")
    var msg: String? = null
)
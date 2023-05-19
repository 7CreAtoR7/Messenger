package ru.ilya.messenger.data.network.messagesDTO

import com.google.gson.annotations.SerializedName

data class SendMessageResponseDto(

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("result")
    val result: String? = null

)

package ru.ilya.messenger.data.network.usersDTO.userStatusDTO

import com.google.gson.annotations.SerializedName


data class WebsiteDto(

    @SerializedName("status")
    var status: String? = null,

    @SerializedName("timestamp")
    var timestamp: Int? = null
)

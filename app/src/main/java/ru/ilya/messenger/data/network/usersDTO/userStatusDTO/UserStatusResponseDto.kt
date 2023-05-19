package ru.ilya.messenger.data.network.usersDTO.userStatusDTO

import com.google.gson.annotations.SerializedName


data class UserStatusResponseDto(

    @SerializedName("result")
    var result: String? = null,

    @SerializedName("msg")
    var msg: String? = null,

    @SerializedName("presence")
    var presence: PresenceDto = PresenceDto()
)
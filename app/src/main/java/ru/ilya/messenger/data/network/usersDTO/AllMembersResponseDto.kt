package ru.ilya.messenger.data.network.usersDTO

import com.google.gson.annotations.SerializedName


data class AllMembersResponseDto(

    @SerializedName("result")
    var result: String? = null,

    @SerializedName("msg")
    var msg: String? = null,

    @SerializedName("members")
    var members: List<UserResponseDto> = emptyList()

)
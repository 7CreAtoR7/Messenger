package ru.ilya.messenger.data.network.usersDTO.userStatusDTO

import com.google.gson.annotations.SerializedName


data class PresenceDto(

    @SerializedName("aggregated")
    var aggregated: AggregatedDto = AggregatedDto(),

    @SerializedName("website")
    var website: WebsiteDto = WebsiteDto()
)
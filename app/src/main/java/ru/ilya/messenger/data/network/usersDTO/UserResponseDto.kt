package ru.ilya.messenger.data.network.usersDTO

import com.google.gson.annotations.SerializedName


data class UserResponseDto(

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("user_id")
    var userId: Int? = null,

    @SerializedName("avatar_version")
    var avatarVersion: Int? = null,

    @SerializedName("is_admin")
    var isAdmin: Boolean? = null,

    @SerializedName("is_owner")
    var isOwner: Boolean? = null,

    @SerializedName("is_guest")
    var isGuest: Boolean? = null,

    @SerializedName("is_billing_admin")
    var isBillingAdmin: Boolean? = null,

    @SerializedName("role")
    var role: Int? = null,

    @SerializedName("is_bot")
    var isBot: Boolean? = null,

    @SerializedName("full_name")
    var fullName: String? = null,

    @SerializedName("timezone")
    var timezone: String? = null,

    @SerializedName("is_active")
    var isActive: Boolean? = null,

    @SerializedName("date_joined")
    var dateJoined: String? = null,

    @SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @SerializedName("delivery_email")
    var deliveryEmail: String? = null
)
package ru.ilya.messenger.domain.entities

data class UserPeople(
    var email: String,
    var userId: Int,
    var fullName: String,
    var timezone: String,
    var isActive: Boolean,
    var dateJoined: String,
    var avatarUrl: String,
    var deliveryEmail: String,
    var status: String
)
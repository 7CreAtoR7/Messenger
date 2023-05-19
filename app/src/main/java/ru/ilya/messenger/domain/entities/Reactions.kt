package ru.ilya.messenger.domain.entities

data class Reactions(
    val emoji_name: String,
    val emoji_code: String,
    val reaction_type: String,
    val user: User,
    val user_id: Int
)
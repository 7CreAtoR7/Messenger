package ru.ilya.messenger.domain.entities

data class User(
    val email: String,
    val id: Int,
    val full_name: String
)
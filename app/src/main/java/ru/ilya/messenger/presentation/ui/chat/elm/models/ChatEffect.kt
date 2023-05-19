package ru.ilya.messenger.presentation.ui.chat.elm.models

sealed class ChatEffect {

    data class AddMessageReactionError(val errorMessage: String): ChatEffect()
}
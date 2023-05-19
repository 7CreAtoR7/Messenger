package ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models

sealed class SubscribedEffect {

    data class SubscribedStreamsError(val errorMessage: String) : SubscribedEffect()
}
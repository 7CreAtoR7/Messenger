package ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models

sealed class AllStreamsEffect {

    data class AllStreamsError(val errorMessage: String): AllStreamsEffect()
}
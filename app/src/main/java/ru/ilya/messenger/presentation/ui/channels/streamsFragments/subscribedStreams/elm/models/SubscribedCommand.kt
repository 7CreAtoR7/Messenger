package ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models

import ru.ilya.messenger.domain.entities.StreamData

sealed class SubscribedCommand {

    data class LoadStreamList(val auth: String) : SubscribedCommand()

    data class LoadStreamListFromDb(val isSubscribed: Boolean) : SubscribedCommand()

    data class AddStreamToDb(val streamList: List<StreamData>) : SubscribedCommand()

    data class LoadStreamBySearch(val auth: String, val query: String) : SubscribedCommand()

    data class LoadStreamTopicListForSearch(
        val query: String,
        val auth: String,
        val streamList: List<StreamData>
    ) : SubscribedCommand()

    data class LoadStreamTopicList(
        val auth: String,
        val streamList: List<StreamData>
    ) : SubscribedCommand()

}
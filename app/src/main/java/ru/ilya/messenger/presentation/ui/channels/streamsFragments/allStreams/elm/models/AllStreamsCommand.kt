package ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models

import ru.ilya.messenger.domain.entities.StreamAllModel


sealed class AllStreamsCommand {

    data class LoadStreamList(val auth: String) : AllStreamsCommand()

    data class LoadStreamListFromDb(val isSubscribed: Boolean) : AllStreamsCommand()

    data class AddStreamToDb(val streamList: List<StreamAllModel>) : AllStreamsCommand()

    data class LoadStreamBySearch(val auth: String, val query: String) : AllStreamsCommand()

    data class LoadStreamTopicListForSearch(
        val query: String,
        val auth: String,
        val streamList: List<StreamAllModel>
    ) : AllStreamsCommand()

    data class LoadStreamTopicList(
        val auth: String,
        val streamList: List<StreamAllModel>
    ) : AllStreamsCommand()

}
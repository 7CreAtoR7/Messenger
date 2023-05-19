package ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models

import ru.ilya.messenger.domain.entities.StreamAllModel

sealed class AllStreamsEvent {
    sealed class Ui : AllStreamsEvent() {

        object LoadStreams : Ui()

        object LoadStreamsFromDb : Ui()

        data class AddStreamsFromServerToDb(val streamsWithTopics: List<StreamAllModel>) : Ui()

        data class LoadStreamBySearch(val auth: String, val query: String): Ui()
    }

    // результат команды загрузки стримов и топиков - сам список либо ошибка
    sealed class Internal : AllStreamsEvent() {

        data class StreamsLoaded(val streams: List<StreamAllModel>) : Internal()

        data class StreamsLoadedForSearch(val query: String, val streams: List<StreamAllModel>) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()

        data class StreamsWithTopicsLoaded(val streamsWithTopicsList: List<StreamAllModel>) : Internal()

        data class StreamsWithTopicsLoadedForSearch(val streamsWithTopicsListForSearch: List<StreamAllModel>) : Internal()

        data class StreamsWithTopicsFromDbLoaded(val streamsWithTopicsList: List<StreamAllModel>) :
            Internal()

        object Loading : Internal()

    }
}

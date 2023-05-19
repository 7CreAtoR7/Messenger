package ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models

import ru.ilya.messenger.domain.entities.StreamData

sealed class SubscribedEvent {
    sealed class Ui : SubscribedEvent() {

        object LoadStreams : Ui()

        object LoadStreamsFromDb : Ui()

        data class AddStreamsFromServerToDb(val streamsWithTopics: List<StreamData>) : Ui()

        data class LoadStreamBySearch(val auth: String, val query: String) : Ui()
    }

    // результат команды загрузки стримов и топиков - сам список либо ошибка
    sealed class Internal : SubscribedEvent() {

        data class StreamsLoaded(val streams: List<StreamData>) : Internal()

        data class StreamsLoadedForSearch(val query: String, val streams: List<StreamData>) :
            Internal()

        data class ErrorLoading(val error: Throwable) : Internal()

        data class StreamsWithTopicsLoaded(val streamsWithTopicsList: List<StreamData>) : Internal()

        data class StreamsWithTopicsLoadedForSearch(val streamsWithTopicsListForSearch: List<StreamData>) :
            Internal()


        data class StreamsWithTopicsFromDbLoaded(val streamsWithTopicsList: List<StreamData>) :
            Internal()

        object Loading : Internal()
    }
}

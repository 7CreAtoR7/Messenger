package ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models

import ru.ilya.messenger.domain.entities.StreamData

// на экране SubscribedFragment 2 состояния:
// загрузка (стримов/топиков и загрузка ПОИСКА) и отображение загруженного списка стримов/топиков
data class SubscribedState(
    val isLoading: Boolean = false,
    val streams: List<StreamData> = emptyList(),
    val streamFromServer: List<StreamData> = emptyList(),
    val error: Throwable? = null,
    val isLoadingSearch: Boolean = false
)
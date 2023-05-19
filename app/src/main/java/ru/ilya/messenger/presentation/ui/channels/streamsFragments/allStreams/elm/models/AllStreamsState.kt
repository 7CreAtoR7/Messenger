package ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models

import ru.ilya.messenger.domain.entities.StreamAllModel

// на экране AllStreamsFragment 2 состояния:
// загрузка (стримов/топиков и загрузка ПОИСКА) и отображение загруженного списка стримов/топиков
data class AllStreamsState(
    val isLoading: Boolean = false,
    val isLoadingSearch: Boolean = false,
    val streams: List<StreamAllModel> = emptyList(),
    val streamFromServer: List<StreamAllModel> = emptyList(),
    val error: Throwable? = null
)

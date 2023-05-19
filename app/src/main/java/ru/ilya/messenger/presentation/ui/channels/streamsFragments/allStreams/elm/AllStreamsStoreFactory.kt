package ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm

import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsState
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject


class AllStreamsStoreFactory @Inject constructor(
    private val allStreamsActor: AllStreamsActor
) {

    private val allStreamsStore by lazy {
        ElmStoreCompat(
            initialState = AllStreamsState(),
            reducer = AllStreamsReducer(),
            actor = allStreamsActor
        )
    }

    fun provide() = allStreamsStore
}

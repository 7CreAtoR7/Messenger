package ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm

import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedState
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject


class SubscribedStoreFactory @Inject constructor(
    private val subscribedActor: SubscribedActor
) {

    private val subscribedStore by lazy {
        ElmStoreCompat(
            initialState = SubscribedState(),
            reducer = SubscribedReducer(),
            actor = subscribedActor
        )
    }

    fun provide() = subscribedStore
}

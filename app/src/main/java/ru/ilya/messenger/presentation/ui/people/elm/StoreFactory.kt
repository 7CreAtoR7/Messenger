package ru.ilya.messenger.presentation.ui.people.elm

import ru.ilya.messenger.presentation.ui.people.elm.models.State
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject

class StoreFactory @Inject constructor(
    private val actor: PeopleActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = State(),
            reducer = PeopleReducer(),
            actor = actor
        )
    }

    fun provide() = store
}

package ru.ilya.messenger.presentation.ui.chat.elm

import ru.ilya.messenger.presentation.ui.chat.elm.models.ChatState
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ChatStoreFactory(
    private val chatActor: ChatActor
) {

    private val chatStore by lazy {
        ElmStoreCompat(
            initialState = ChatState(),
            reducer = ChatReducer(),
            actor = chatActor
        )
    }

    fun provide() = chatStore
}

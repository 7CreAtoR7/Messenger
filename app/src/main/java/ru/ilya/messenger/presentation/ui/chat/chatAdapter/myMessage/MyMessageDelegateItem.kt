package ru.ilya.messenger.presentation.ui.chat.chatAdapter.myMessage

import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.DelegateItem

class MyMessageDelegateItem(
    private val value: MessageModel
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = 0

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as MyMessageDelegateItem).value == content()
    }
}

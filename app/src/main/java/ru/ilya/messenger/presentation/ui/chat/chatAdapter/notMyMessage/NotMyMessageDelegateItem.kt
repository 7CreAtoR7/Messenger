package ru.ilya.messenger.presentation.ui.chat.chatAdapter.notMyMessage

import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.DelegateItem

class NotMyMessageDelegateItem(
    private val value: MessageModel
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = 0

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as NotMyMessageDelegateItem).value == content()
    }
}

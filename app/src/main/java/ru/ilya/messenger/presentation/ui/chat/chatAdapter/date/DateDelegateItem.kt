package ru.ilya.messenger.presentation.ui.chat.chatAdapter.date

import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.DelegateItem

class DateDelegateItem(
    private val value: DateModel
) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int {
        return 0
    }

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as DateDelegateItem).value == content()
    }
}

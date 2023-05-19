package ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils

interface DelegateItem {
    fun content(): Any
    fun id(): Int
    fun compareToOther(other: DelegateItem): Boolean
}
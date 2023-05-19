package ru.ilya.messenger.presentation.ui.chat

import android.view.View

interface ClickReaction {

    fun onEmojiInBottomSheetClick(messageId: Int, emojiName: String)

    fun onEmojiClicked(view: View, messageId: Int)
}
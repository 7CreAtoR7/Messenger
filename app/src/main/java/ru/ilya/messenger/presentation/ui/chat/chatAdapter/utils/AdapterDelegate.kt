package ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface AdapterDelegate {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem, position: Int, itemCount: Int)
    fun isOfViewType(item: DelegateItem): Boolean
}

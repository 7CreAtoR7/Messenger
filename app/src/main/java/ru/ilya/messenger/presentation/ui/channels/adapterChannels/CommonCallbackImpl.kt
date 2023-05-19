package ru.ilya.messenger.presentation.ui.channels.adapterChannels

import androidx.recyclerview.widget.DiffUtil
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.expandableItem.ExpandableItem

class CommonCallbackImpl(
    private val oldItems: List<ListItem>,
    private val newItems: List<ListItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newItems[newItemPosition]
        val oldItem = oldItems[oldItemPosition]
        return when {
            newItem is ExpandableItem && oldItem is ExpandableItem ->
                newItem.name == oldItem.name && newItem.innerItems == oldItem.innerItems
            else -> newItem == oldItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any =
        { Any() } // для старта анимации стрелки
}

package ru.ilya.messenger.presentation.ui.channels.adapterChannels.expandableItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat.animate
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ilya.messenger.R
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.ListItem

class ExpandableItemDelegate(
    private val clickCallback: (expandableItem: ExpandableItem) -> Unit
) : AbsListItemAdapterDelegate<ExpandableItem, ListItem, ExpandableItemDelegate.ExpandableItemViewHolder>() {

    inner class ExpandableItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val streamTitleTextView: TextView = view.findViewById(R.id.streamTitle)
        val arrow: ImageView = view.findViewById(R.id.arrow)
        val animation = animate(arrow)
    }

    override fun isForViewType(
        item: ListItem,
        items: MutableList<ListItem>,
        position: Int
    ): Boolean {
        return item is ExpandableItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): ExpandableItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.stream_row, parent, false)
        return ExpandableItemViewHolder(view)
    }

    override fun onBindViewHolder(
        item: ExpandableItem,
        holder: ExpandableItemViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.arrow.setOnClickListener {
            clickCallback(item)
        }
        holder.streamTitleTextView.text =
            holder.itemView.context.getString(R.string.stream_title_start_name, item.name)
        if (payloads.isNotEmpty()) {
            holder.animation.rotation(if (item.isExpanded) 180f else 0f).start()
        } else {
            holder.arrow.rotation = if (item.isExpanded) 180f else 0f
        }
    }
}

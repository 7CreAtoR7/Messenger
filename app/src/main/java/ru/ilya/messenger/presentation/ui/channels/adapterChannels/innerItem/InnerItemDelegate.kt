package ru.ilya.messenger.presentation.ui.channels.adapterChannels.innerItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ilya.messenger.R
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.ListItem
import ru.ilya.messenger.util.Constants
import kotlin.random.Random

class InnerItemDelegate(
    private val clickTopicCallback: (innerItem: InnerItem) -> Unit
) : AbsListItemAdapterDelegate<InnerItem, ListItem, InnerItemDelegate.InnerItemViewHolder>() {

    inner class InnerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val topicTitleTextView: TextView = view.findViewById(R.id.topicTitle)
    }

    override fun isForViewType(
        item: ListItem,
        items: MutableList<ListItem>,
        position: Int
    ): Boolean {
        return item is InnerItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): InnerItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.topic_row, parent, false)
        return InnerItemViewHolder(view)
    }

    override fun onBindViewHolder(
        item: InnerItem,
        holder: InnerItemViewHolder,
        payloads: MutableList<Any>
    ) {

        holder.itemView.setOnClickListener {
            clickTopicCallback(item)
        }

        val listColors = listOf(R.color.dirty_blue, R.color.dirty_yellow)
        val randomColor = listColors[Random.nextInt(Constants.MAX_RANDOM_VALUE_FOR_COLOR)]
        holder.itemView.setBackgroundColor(holder.itemView.context.getColor(randomColor))
        holder.itemView.parent
        holder.topicTitleTextView.text = item.name
    }
}

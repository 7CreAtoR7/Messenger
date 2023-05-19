package ru.ilya.messenger.presentation.ui.channels.adapterChannels

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.ilya.messenger.domain.entities.TopicData
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.expandableItem.ExpandableItem
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.expandableItem.ExpandableItemDelegate
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.innerItem.InnerItem
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.innerItem.InnerItemDelegate
import ru.ilya.messenger.util.expand

class StreamsAdapter :
    ListDelegationAdapter<List<ListItem>>() {

    private var sourceList: List<ListItem> = emptyList()

    var onTopicClickListener: ((TopicData) -> Unit)? = null
    var onArrowClickListener: ((String, Int) -> Unit)? = null

    init {
        delegatesManager.addDelegate(ExpandableItemDelegate { item: ExpandableItem ->
            onArrowClickListener?.invoke(item.name, item.streamId) // передаем название и id стрима

            val newSourceList = sourceList.toMutableList()
            val itemIndex = sourceList.indexOf(item)
            newSourceList[itemIndex] = item.copy(isExpanded = !item.isExpanded)
            setItems(newSourceList)
        })
        delegatesManager.addDelegate(InnerItemDelegate { innerTopicItem: InnerItem ->
            onTopicClickListener?.invoke(
                TopicData(
                    name = innerTopicItem.name,
                )
            )
        })
    }

    override fun setItems(list: List<ListItem>?) {
        this.sourceList = list!!
        val oldItems = items ?: emptyList()
        val newItems = list.expand()
        val diffResult: DiffUtil.DiffResult = calculateDiff(oldItems, newItems)
        diffResult.dispatchUpdatesTo(this)
        super.setItems(newItems)
    }

    private fun calculateDiff(
        oldItems: List<ListItem>,
        newItems: List<ListItem>
    ): DiffUtil.DiffResult {
        val commonCallbackImpl = CommonCallbackImpl(
            oldItems = oldItems,
            newItems = newItems,
        )
        return DiffUtil.calculateDiff(commonCallbackImpl)
    }
}
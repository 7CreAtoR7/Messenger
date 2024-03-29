package ru.ilya.messenger.domain.entities

import ru.ilya.messenger.presentation.ui.channels.adapterChannels.expandableItem.ExpandableItem
import ru.ilya.messenger.util.mapListTopicToListInnerItem

data class StreamAllModel(

    val streamId: Int,
    val name: String,
    var topicList: List<TopicData> = emptyList(),
    var isExpanded: Boolean = false

) : StreamModel {
    fun toExpandableItem() = ExpandableItem(
        streamId = streamId,
        name = name,
        isExpanded = isExpanded,
        innerItems = mapListTopicToListInnerItem(topicList)
    )
}
package ru.ilya.messenger.presentation.ui.channels.adapterChannels.expandableItem

import ru.ilya.messenger.presentation.ui.channels.adapterChannels.ListItem
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.innerItem.InnerItem

data class ExpandableItem(
    val streamId: Int,
    val name: String,
    val isExpanded: Boolean = false,
    var innerItems: List<InnerItem> = emptyList()
) : ListItem
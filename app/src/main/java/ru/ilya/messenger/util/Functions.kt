package ru.ilya.messenger.util

import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.domain.entities.StreamAllModel
import ru.ilya.messenger.domain.entities.TopicData
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.ListItem
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.expandableItem.ExpandableItem
import ru.ilya.messenger.presentation.ui.channels.adapterChannels.innerItem.InnerItem
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.date.DateDelegateItem
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.date.DateModel
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.myMessage.MyMessageDelegateItem
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.notMyMessage.NotMyMessageDelegateItem
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.DelegateItem
import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset

fun LocalDate.formatDateToDayMonthString(): String {
    val day = dayOfMonth
    val monthStringName = when (month!!) {
        Month.JANUARY -> "января"
        Month.FEBRUARY -> "февраля"
        Month.MARCH -> "марта"
        Month.APRIL -> "апреля"
        Month.MAY -> "мая"
        Month.JUNE -> "июня"
        Month.JULY -> "июля"
        Month.AUGUST -> "августа"
        Month.SEPTEMBER -> "сентября"
        Month.OCTOBER -> "октября"
        Month.NOVEMBER -> "ноября"
        Month.DECEMBER -> "декабря"
    }
    return "$day $monthStringName"
}


fun timestampToLocalDate(timestamp: Long): LocalDate {
    return LocalDate.ofEpochDay(timestamp / 86400).atStartOfDay(ZoneOffset.UTC).toLocalDate()
}

fun List<ListItem>.expand() = flatMap { item ->
    if (item is ExpandableItem && item.isExpanded)
        listOf(item) + item.innerItems
    else
        listOf(item)
}

fun mapListTopicToListInnerItem(list: List<TopicData>): List<InnerItem> {
    return list.map {
        it.toInnerItem()
    }
}

fun mapListStreamAllModelToListExpandableItem(list: List<StreamAllModel>): List<ExpandableItem> {
    return list.map {
        it.toExpandableItem()
    }
}

fun List<MessageModel>.concatenateWithDate(dates: List<DateModel>): List<DelegateItem> {

    val delegateItemList: MutableList<DelegateItem> = mutableListOf()

    dates.forEach { dateModel ->
        delegateItemList.add(
            DateDelegateItem(value = dateModel)
        )

        val date = dateModel.date
        val currentDayMessages = this.filter { message ->
            timestampToLocalDate(message.timestamp).formatDateToDayMonthString() == date
        }

        currentDayMessages.forEach { model ->
            val messageTypeItem =
                if (model.senderId != 604430) NotMyMessageDelegateItem(value = model)
                else MyMessageDelegateItem(value = model)
            delegateItemList.add(messageTypeItem)
        }
    }
    return delegateItemList
}

package ru.ilya.messenger.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class TopicData(

    val name: String,
    val topicMessages: Int = 0
) : Parcelable {
//    fun toInnerItem(): InnerItem {
//        return InnerItem(
//            name = name
//        )
//    }
}
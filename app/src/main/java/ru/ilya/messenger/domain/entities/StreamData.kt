package ru.ilya.messenger.domain.entities

data class StreamData(

    val streamId: Int,
    val name: String,
    var topicList: List<TopicData> = emptyList(),
    var isExpanded: Boolean = false

) : StreamModel {

    fun toStreamAllModel() = StreamAllModel(
        streamId = streamId,
        name = name,
        topicList = topicList,
        isExpanded = isExpanded
    )
}

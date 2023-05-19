package ru.ilya.messenger.domain.entities


data class StreamsWithTopicsEntityModel(
    var id: Int = UNDEFINED_ID,
    val streamId: Int,
    val streamName: String,
    val isSubscribed: Boolean,
    val topicName: String
) {

    companion object {

        const val UNDEFINED_ID = 0
    }
}
package ru.ilya.messenger.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AllStreamsWithTopics")
data class AllStreamsWithTopicsDbModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "stream_id")
    val streamId: Int,

    @ColumnInfo(name = "stream_name")
    val streamName: String,

    @ColumnInfo(name = "is_subscribed")
    val isSubscribed: Boolean,

    @ColumnInfo(name = "topic_name")
    val topicName: String
)
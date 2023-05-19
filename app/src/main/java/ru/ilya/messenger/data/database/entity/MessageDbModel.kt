package ru.ilya.messenger.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.ilya.messenger.data.database.converters.ConverterReactionsList
import ru.ilya.messenger.domain.entities.Reactions

@Entity(tableName = "Messages")
data class MessageDbModel(

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String,

    @ColumnInfo(name = "content")
    var content: String,

    @TypeConverters(ConverterReactionsList::class)
    var reactions: List<Reactions> = emptyList(),

    @ColumnInfo(name = "sender_email")
    var senderEmail: String,

    @ColumnInfo(name = "sender_full_name")
    var senderFullName: String,

    @ColumnInfo(name = "sender_id")
    var senderId: Int,

    @ColumnInfo(name = "stream_id")
    var streamId: Int,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long
)
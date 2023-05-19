package ru.ilya.messenger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ilya.messenger.data.database.entity.MessageDbModel

@Dao
interface MessagesDao {

    @Query("SELECT * FROM Messages WHERE stream_id = :streamId ORDER BY timestamp ASC LIMIT 50")
    suspend fun getMessagesFromDb(streamId: Int): List<MessageDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessagesInDb(listMessageDbModel: List<MessageDbModel>)

}
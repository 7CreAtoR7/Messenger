package ru.ilya.messenger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ilya.messenger.data.database.entity.AllStreamsWithTopicsDbModel

@Dao
interface AllStreamsDao {

    @Query("SELECT * FROM AllStreamsWithTopics WHERE is_subscribed = :isSubscribed")
    suspend fun getAllStreamsWithTopicsFromDb(isSubscribed: Boolean): List<AllStreamsWithTopicsDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllStreamWithTopicsInDb(listAllStreamsWithTopicsDbModel: List<AllStreamsWithTopicsDbModel>)

}
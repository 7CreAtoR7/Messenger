package ru.ilya.messenger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ilya.messenger.data.database.entity.SubscribedStreamsWithTopicsDbModel

@Dao
interface SubscribedStreamsDao {

    @Query("SELECT * FROM StreamsWithTopics WHERE is_subscribed = :isSubscribed")
    suspend fun getSubscribedStreamsWithTopicsFromDb(isSubscribed: Boolean): List<SubscribedStreamsWithTopicsDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubscribedStreamWithTopicsInDb(listSubscribedStreamsWithTopicsDbModel: List<SubscribedStreamsWithTopicsDbModel>)

}
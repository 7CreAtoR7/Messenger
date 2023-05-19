package ru.ilya.messenger.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.ilya.messenger.data.database.converters.ConverterReactionsList
import ru.ilya.messenger.data.database.dao.AllStreamsDao
import ru.ilya.messenger.data.database.dao.MessagesDao
import ru.ilya.messenger.data.database.dao.SubscribedStreamsDao
import ru.ilya.messenger.data.database.entity.AllStreamsWithTopicsDbModel
import ru.ilya.messenger.data.database.entity.MessageDbModel
import ru.ilya.messenger.data.database.entity.SubscribedStreamsWithTopicsDbModel

@Database(
    entities = [MessageDbModel::class, SubscribedStreamsWithTopicsDbModel::class, AllStreamsWithTopicsDbModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConverterReactionsList::class)
abstract class AppDatabase : RoomDatabase() {

    // абстрактный метод, возвращающий реализацию Dao для SubscribedStreams
    abstract fun subscribedStreamsDao(): SubscribedStreamsDao

    abstract fun allStreamsDao(): AllStreamsDao

    abstract fun messagesDao(): MessagesDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "Messenger.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}
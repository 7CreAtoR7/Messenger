package ru.ilya.messenger.di

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.ilya.messenger.data.database.AppDatabase
import ru.ilya.messenger.data.database.dao.AllStreamsDao
import ru.ilya.messenger.data.database.dao.MessagesDao
import ru.ilya.messenger.data.database.dao.SubscribedStreamsDao

@Module
class DataModule {

    @ApplicationScope
    @Provides
    fun provideSubscribedStreamsDao(
        application: Application
    ): SubscribedStreamsDao {
        return AppDatabase.getInstance(application).subscribedStreamsDao()
    }

    @ApplicationScope
    @Provides
    fun provideAllStreamsDao(
        application: Application
    ): AllStreamsDao {
        return AppDatabase.getInstance(application).allStreamsDao()
    }

    @ApplicationScope
    @Provides
    fun provideMessagesDao(
        application: Application
    ): MessagesDao {
        return AppDatabase.getInstance(application).messagesDao()
    }


}

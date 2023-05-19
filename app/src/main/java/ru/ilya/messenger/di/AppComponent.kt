package ru.ilya.messenger.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.AllStreamsFragment
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.AllStreamsStoreFactory
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.SubscribedStreamsFragment
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.SubscribedStoreFactory
import ru.ilya.messenger.presentation.ui.chat.MessageChatFragment
import ru.ilya.messenger.presentation.ui.people.PeopleFragment
import ru.ilya.messenger.presentation.ui.people.elm.StoreFactory

@ApplicationScope
@Component(modules = [AppModule::class, NetworkModule::class, DataModule::class])

interface AppComponent {

    fun inject(fragment: SubscribedStreamsFragment)

    fun inject(fragment: AllStreamsFragment)

    fun inject(fragment: PeopleFragment)

    fun inject(fragment: MessageChatFragment)

    fun provideSubscribedStoreFactory(): SubscribedStoreFactory

    fun provideAllStoreFactory(): AllStreamsStoreFactory

    fun providePeopleStoreFactory(): StoreFactory

    fun provideChatStoreFactory(): StoreFactory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}
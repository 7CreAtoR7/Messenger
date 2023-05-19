package ru.ilya.messenger.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ilya.messenger.data.repositoryImpl.MessengerRepositoryImpl
import ru.ilya.messenger.domain.repository.MessengerRepository
import ru.ilya.messenger.domain.useCases.*
import ru.ilya.messenger.domain.useCases.dbUseCases.*
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.AllStreamsActor
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.AllStreamsStoreFactory
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.SubscribedActor
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.SubscribedStoreFactory
import ru.ilya.messenger.presentation.ui.chat.elm.ChatActor
import ru.ilya.messenger.presentation.ui.chat.elm.ChatStoreFactory
import ru.ilya.messenger.presentation.ui.people.elm.PeopleActor
import ru.ilya.messenger.presentation.ui.people.elm.StoreFactory
import ru.ilya.messenger.di.ApplicationScope

@Module(includes = [AppModule.Bind::class])
class AppModule {

    @Module
    interface Bind {

        @Binds
        @ApplicationScope
        fun bindMessengerRepository(impl: MessengerRepositoryImpl): MessengerRepository
    }

    // People экран
    @Provides
    fun provideLoadUsersUseCase(repository: MessengerRepository): LoadUsersUseCase {
        return LoadUsersUseCase(repository)
    }

    @Provides
    fun provideLoadUserStatusUseCase(repository: MessengerRepository): LoadUserStatusUseCase {
        return LoadUserStatusUseCase(repository)
    }

    @Provides
    fun providePeopleActor(
        loadUsers: LoadUsersUseCase,
        loadUserStatus: LoadUserStatusUseCase
    ): PeopleActor {
        return PeopleActor(loadUsers, loadUserStatus)
    }

    @Provides
    @ApplicationScope
    fun provideStoreFactory(actor: PeopleActor): StoreFactory {
        return StoreFactory(actor)
    }

    // SubscribedStreams экран
    @Provides
    fun provideSubscribedStreamsUseCase(repository: MessengerRepository): GetSubscribedStreamsListUseCase {
        return GetSubscribedStreamsListUseCase(repository)
    }

    @Provides
    fun provideGetStreamsFromDbUseCase(repository: MessengerRepository): GetStreamsFromDbUseCase {
        return GetStreamsFromDbUseCase(repository)
    }

    @Provides
    fun provideAddStreamWithTopicsInDbUseCase(repository: MessengerRepository): AddStreamWithTopicsInDbUseCase {
        return AddStreamWithTopicsInDbUseCase(repository)
    }

    @Provides
    fun provideStreamTopicsUseCase(repository: MessengerRepository): GetStreamTopicsUseCase {
        return GetStreamTopicsUseCase(repository)
    }

    @Provides
    fun provideSubscribedActor(
        getSubscribed: GetSubscribedStreamsListUseCase,
        getTopics: GetStreamTopicsUseCase,
        getStreamsFromDbUseCase: GetStreamsFromDbUseCase,
        addStreamWithTopicsInDbUseCase: AddStreamWithTopicsInDbUseCase
    ): SubscribedActor {
        return SubscribedActor(
            getSubscribed,
            getTopics,
            getStreamsFromDbUseCase,
            addStreamWithTopicsInDbUseCase
        )
    }

    @Provides
    @ApplicationScope
    fun provideSubscribedStoreFactory(actor: SubscribedActor): SubscribedStoreFactory {
        return SubscribedStoreFactory(actor)
    }

    // AllStreams экран
    @Provides
    fun provideAllStreamsUseCase(repository: MessengerRepository): GetAllStreamsListUseCase =
        GetAllStreamsListUseCase(repository)

    @Provides
    fun provideAllActor(
        getAll: GetAllStreamsListUseCase,
        getTopics: GetStreamTopicsUseCase,
        getAllStreamsFromDbUseCase: GetAllStreamsFromDbUseCase,
        addAllStreamWithTopicsInDbUseCase: AddAllStreamWithTopicsInDbUseCase
    ): AllStreamsActor {
        return AllStreamsActor(
            getAll,
            getTopics,
            getAllStreamsFromDbUseCase,
            addAllStreamWithTopicsInDbUseCase
        )
    }

    @Provides
    @ApplicationScope
    fun provideAllStoreFactory(actor: AllStreamsActor): AllStreamsStoreFactory {
        return AllStreamsStoreFactory(actor)
    }

    // MessageChat экран
    @Provides
    fun provideGetTopicMessagesUseCase(repository: MessengerRepository): GetTopicMessagesUseCase {
        return GetTopicMessagesUseCase(repository)
    }

    @Provides
    fun provideSendMessageToTopicUseCase(repository: MessengerRepository): SendMessageToTopicUseCase {
        return SendMessageToTopicUseCase(repository)
    }

    @Provides
    fun provideAddMessageReactionUseCase(repository: MessengerRepository): AddMessageReactionUseCase {
        return AddMessageReactionUseCase(repository)
    }

    @Provides
    fun provideDeleteMessageReactionUseCase(repository: MessengerRepository): DeleteMessageReactionUseCase {
        return DeleteMessageReactionUseCase(repository)
    }

    @Provides
    fun provideGetMessagesFromDbUseCase(repository: MessengerRepository): GetMessagesFromDbUseCase {
        return GetMessagesFromDbUseCase(repository)
    }

    @Provides
    fun provideAddMessagesInDbUseCase(repository: MessengerRepository): AddMessagesInDbUseCase {
        return AddMessagesInDbUseCase(repository)
    }

    @Provides
    fun provideChatActor(
        getTopicMessagesUseCase: GetTopicMessagesUseCase,
        sendMessageToTopicUseCase: SendMessageToTopicUseCase,
        addMessageReactionUseCase: AddMessageReactionUseCase,
        deleteMessageReactionUseCase: DeleteMessageReactionUseCase,
        getMessagesFromDbUseCase: GetMessagesFromDbUseCase,
        addMessagesInDbUseCase: AddMessagesInDbUseCase
    ): ChatActor {
        return ChatActor(
            getTopicMessagesUseCase,
            sendMessageToTopicUseCase,
            addMessageReactionUseCase,
            deleteMessageReactionUseCase,
            getMessagesFromDbUseCase,
            addMessagesInDbUseCase
        )
    }

    @Provides
    @ApplicationScope
    fun provideChatStoreFactory(actor: ChatActor): ChatStoreFactory {
        return ChatStoreFactory(actor)
    }
}
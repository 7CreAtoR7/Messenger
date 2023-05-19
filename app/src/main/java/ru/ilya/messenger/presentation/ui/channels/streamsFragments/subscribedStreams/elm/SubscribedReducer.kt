package ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm

import okhttp3.Credentials
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedCommand
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedEffect
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedEvent
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedState
import ru.ilya.messenger.util.Constants
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class SubscribedReducer :
    DslReducer<SubscribedEvent, SubscribedState, SubscribedEffect, SubscribedCommand>() {

    override fun Result.reduce(event: SubscribedEvent): Any = when (event) {
        is SubscribedEvent.Ui.LoadStreams -> {
            commands {
                +SubscribedCommand.LoadStreamList(
                    Credentials.basic(
                        Constants.EMAIL,
                        Constants.PASSWORD
                    )
                )
            }
        }

        is SubscribedEvent.Ui.LoadStreamBySearch -> {
            state { copy(isLoadingSearch = true, error = null, streams = emptyList()) }
            commands {
                +SubscribedCommand.LoadStreamBySearch(
                    auth = event.auth,
                    query = event.query
                )
            }
        }

        is SubscribedEvent.Internal.StreamsLoadedForSearch -> {
            // теперь получаем топики для стримов для поиска
            commands {
                +SubscribedCommand.LoadStreamTopicListForSearch(
                    query = event.query,
                    auth = Credentials.basic(Constants.EMAIL, Constants.PASSWORD),
                    streamList = event.streams
                )
            }
        }

        is SubscribedEvent.Internal.StreamsWithTopicsLoadedForSearch -> {
            // пришшел список со стримами и их топиками в поиске
            state {
                copy(
                    isLoadingSearch = false,
                    streamFromServer = event.streamsWithTopicsListForSearch
                )
            }
        }

        is SubscribedEvent.Ui.LoadStreamsFromDb -> {
            // Экран с подписанными стримами, получаем список из БД
            commands { +SubscribedCommand.LoadStreamListFromDb(isSubscribed = true) }
        }
        is SubscribedEvent.Ui.AddStreamsFromServerToDb -> {
            commands { +SubscribedCommand.AddStreamToDb(streamList = event.streamsWithTopics) }
        }
        is SubscribedEvent.Internal.StreamsWithTopicsFromDbLoaded -> {
            // пришшел список со стримами и их топиками из БД
            state { copy(isLoading = false, streams = event.streamsWithTopicsList) }
        }
        is SubscribedEvent.Internal.StreamsLoaded -> {
            // отправляем команду с получением спсика топиков для каждого стрима из списка
            commands {
                +SubscribedCommand.LoadStreamTopicList(
                    auth = Credentials.basic(Constants.EMAIL, Constants.PASSWORD),
                    streamList = event.streams
                )
            }
        }
        is SubscribedEvent.Internal.StreamsWithTopicsLoaded -> {
            // пришшел список со стримами и их топиками с бэка, обновим наш список стримов на
            // экране, на котором сейчас изображены стримы из бд
            state { copy(isLoading = false, streamFromServer = event.streamsWithTopicsList) }
        }
        is SubscribedEvent.Internal.ErrorLoading -> {
            effects { +SubscribedEffect.SubscribedStreamsError("Ошибка ${event.error.message}") }
            state { copy(isLoading = false) }
        }

        is SubscribedEvent.Internal.Loading -> {}
    }
}
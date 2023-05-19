package ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm


import okhttp3.Credentials
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsCommand
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsEffect
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsEvent
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsState
import ru.ilya.messenger.util.Constants
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class AllStreamsReducer :
    DslReducer<AllStreamsEvent, AllStreamsState, AllStreamsEffect, AllStreamsCommand>() {

    override fun Result.reduce(event: AllStreamsEvent): Any = when (event) {
        is AllStreamsEvent.Ui.LoadStreams -> {
            // При старте экрана старый список очищается при открытии фрагмента
            state { copy(isLoading = true, error = null, streams = emptyList()) }
            commands {
                +AllStreamsCommand.LoadStreamList(
                    Credentials.basic(
                        Constants.EMAIL,
                        Constants.PASSWORD
                    )
                )
            }
        }

        is AllStreamsEvent.Ui.LoadStreamsFromDb -> {
            // Экран со всеми стримами, получаем список из БД
            commands { +AllStreamsCommand.LoadStreamListFromDb(isSubscribed = false) }
        }

        is AllStreamsEvent.Internal.StreamsWithTopicsFromDbLoaded -> {
            // пришшел список со стримами и их топиками из БД
            state { copy(isLoading = false, streams = event.streamsWithTopicsList) }
        }

        is AllStreamsEvent.Ui.AddStreamsFromServerToDb -> {
            commands { +AllStreamsCommand.AddStreamToDb(streamList = event.streamsWithTopics) }
        }

        is AllStreamsEvent.Ui.LoadStreamBySearch -> {
            state { copy(isLoadingSearch = true, error = null, streams = emptyList()) }
            commands {
                +AllStreamsCommand.LoadStreamBySearch(
                    auth = event.auth,
                    query = event.query
                )
            }
        }

        is AllStreamsEvent.Internal.StreamsLoaded -> {
            // отправляем команду с получением спсика топиков для каждого стрима из списка
            commands {
                +AllStreamsCommand.LoadStreamTopicList(
                    auth = Credentials.basic(Constants.EMAIL, Constants.PASSWORD),
                    streamList = event.streams
                )
            }
        }

        is AllStreamsEvent.Internal.StreamsWithTopicsLoaded -> {
            // пришшел список со стримами и их топиками
            state { copy(isLoading = false, streamFromServer = event.streamsWithTopicsList) }
        }

        is AllStreamsEvent.Internal.ErrorLoading -> {
            effects { +AllStreamsEffect.AllStreamsError("Ошибка ${event.error.message}") }
            state { copy(isLoading = false) }
        }

        is AllStreamsEvent.Internal.Loading -> {}

        is AllStreamsEvent.Internal.StreamsLoadedForSearch -> {
            // теперь получаем топики для стримов для поиска
            commands {
                +AllStreamsCommand.LoadStreamTopicListForSearch(
                    query = event.query,
                    auth = Credentials.basic(Constants.EMAIL, Constants.PASSWORD),
                    streamList = event.streams
                )
            }
        }

        is AllStreamsEvent.Internal.StreamsWithTopicsLoadedForSearch -> {
            // пришшел список со стримами и их топиками в поиске
            state {
                copy(
                    isLoadingSearch = false,
                    streamFromServer = event.streamsWithTopicsListForSearch
                )
            }
        }
    }
}
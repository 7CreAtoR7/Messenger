package ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import ru.ilya.messenger.domain.entities.Result
import ru.ilya.messenger.domain.entities.StreamData
import ru.ilya.messenger.domain.entities.StreamsWithTopicsEntityModel
import ru.ilya.messenger.domain.entities.TopicData
import ru.ilya.messenger.domain.useCases.GetStreamTopicsUseCase
import ru.ilya.messenger.domain.useCases.GetSubscribedStreamsListUseCase
import ru.ilya.messenger.domain.useCases.dbUseCases.AddStreamWithTopicsInDbUseCase
import ru.ilya.messenger.domain.useCases.dbUseCases.GetStreamsFromDbUseCase
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedCommand
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.elm.models.SubscribedEvent
import vivid.money.elmslie.core.disposable.Disposable
import vivid.money.elmslie.core.store.DefaultActor
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class SubscribedActor @Inject constructor(
    private val getSubscribedStreamsListUseCase: GetSubscribedStreamsListUseCase,
    private val getStreamTopicsUseCase: GetStreamTopicsUseCase,
    private val getStreamsFromDbUseCase: GetStreamsFromDbUseCase,
    private val addStreamWithTopicsInDbUseCase: AddStreamWithTopicsInDbUseCase
) : Actor<SubscribedCommand, SubscribedEvent>, DefaultActor<Any, Any> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(command: SubscribedCommand): Flow<SubscribedEvent> = when (command) {
        is SubscribedCommand.LoadStreamList -> {
            getSubscribedStreamsListUseCase.invoke(auth = command.auth)
                .flatMapLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            // список стримов (без топиков внутри)
                            flowOf(SubscribedEvent.Internal.StreamsLoaded(result.data))
                        }
                        is Result.Error -> {
                            flowOf(SubscribedEvent.Internal.ErrorLoading(Exception(result.message)))
                        }
                        is Result.Loading -> {
                            flowOf(SubscribedEvent.Internal.Loading)
                        }
                    }
                }
                .catch { e ->
                    emit(SubscribedEvent.Internal.ErrorLoading(e))
                }
                .flowOn(Dispatchers.IO)
        }

        is SubscribedCommand.LoadStreamBySearch -> {
            getSubscribedStreamsListUseCase.invoke(auth = command.auth)
                .flatMapLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            // список стримов (без топиков внутри)
                            flowOf(
                                SubscribedEvent.Internal.StreamsLoadedForSearch(
                                    query = command.query,
                                    result.data
                                )
                            )
                        }
                        is Result.Error -> {
                            flowOf(SubscribedEvent.Internal.ErrorLoading(Exception(result.message)))
                        }
                        is Result.Loading -> {
                            flowOf(SubscribedEvent.Internal.Loading)
                        }
                    }
                }
                .catch { e ->
                    emit(SubscribedEvent.Internal.ErrorLoading(e))
                }
                .flowOn(Dispatchers.IO)
        }

        is SubscribedCommand.LoadStreamTopicListForSearch -> {
            flow<SubscribedEvent> {
                val result = command.streamList
                    .filter { it.name.lowercase().contains(command.query.lowercase()) }
                    .map { stream ->
                        val topics = coroutineScope {
                            async {
                                try {
                                    getStreamTopicsUseCase.invoke(
                                        auth = command.auth,
                                        streamId = stream.streamId
                                    )
                                } catch (e: Exception) {
                                    Result.Error("My error in subscribed actor")
                                }
                            }
                        }
                        stream to topics
                    }.map {
                        val streamInfo = it.first
                        val topicsResult = it.second.await()
                        if (topicsResult is Result.Success) {
                            streamInfo.copy(topicList = topicsResult.data as MutableList<TopicData>)
                        } else {
                            streamInfo
                        }
                    }
                emit(SubscribedEvent.Internal.StreamsWithTopicsLoadedForSearch(result))
            }.catch { e ->
                emit(SubscribedEvent.Internal.ErrorLoading(e))
            }.flowOn(Dispatchers.IO)
        }

        is SubscribedCommand.LoadStreamTopicList -> {
            flow<SubscribedEvent> {
                val result = command.streamList.map { stream ->
                    val topics = coroutineScope {
                        async {
                            try {
                                getStreamTopicsUseCase.invoke(
                                    auth = command.auth,
                                    streamId = stream.streamId
                                )
                            } catch (e: Exception) {
                                Result.Error("My error in subscribed actor")
                            }
                        }
                    }
                    stream to topics
                }.map {
                    val streamInfo = it.first
                    val topicsResult = it.second.await()
                    if (topicsResult is Result.Success) {
                        streamInfo.copy(topicList = topicsResult.data as MutableList<TopicData>)
                    } else {
                        streamInfo
                    }
                }
                emit(SubscribedEvent.Internal.StreamsWithTopicsLoaded(result))
            }.catch { e ->
                emit(SubscribedEvent.Internal.ErrorLoading(e))
            }.flowOn(Dispatchers.IO)
        }
        is SubscribedCommand.LoadStreamListFromDb -> {
            flow<SubscribedEvent> {
                val listStreamsWithTopicsEntityModel =
                    getStreamsFromDbUseCase.invoke(isSubscribed = command.isSubscribed)
                val arrayStreamData = getListStreamDataFromListStreamsWithTopicsEntityModel(
                    listStreamsWithTopicsEntityModel
                )
                emit(SubscribedEvent.Internal.StreamsWithTopicsFromDbLoaded(arrayStreamData))
            }.catch { e ->
                emit(SubscribedEvent.Internal.ErrorLoading(e))
            }.flowOn(Dispatchers.IO)
        }
        is SubscribedCommand.AddStreamToDb -> {
            var idOfRow = 1
            flow<SubscribedEvent> {
                val arrayStreamsWithTopics = ArrayList<StreamsWithTopicsEntityModel>()
                command.streamList.forEach { streamOfStreamData ->
                    // в StreamData есть свойство topicList: List<TopicData>, преобразуем в модель
                    streamOfStreamData.topicList.forEach { topicData ->
                        val streamWithTopicModel = StreamsWithTopicsEntityModel(
                            id = idOfRow++,
                            streamId = streamOfStreamData.streamId,
                            streamName = streamOfStreamData.name,
                            isSubscribed = true,
                            topicName = topicData.name
                        )
                        arrayStreamsWithTopics.add(streamWithTopicModel)
                    }

                }
                addStreamWithTopicsInDbUseCase(listStreamsWithTopicsEntityModel = arrayStreamsWithTopics)
            }.catch {
            }.flowOn(Dispatchers.IO)
        }
    }

    private fun getListStreamDataFromListStreamsWithTopicsEntityModel(
        listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>
    ): List<StreamData> {
        val streamDataList = listStreamsWithTopicsEntityModel.groupBy { it.streamId }
            .map { (streamId, group) ->
                StreamData(
                    streamId = streamId,
                    name = group.first().streamName,
                    topicList = group.map { TopicData(it.topicName) }.toMutableList(),
                    isExpanded = false
                )
            }
        return streamDataList
    }

    override fun execute(
        command: Any,
        onEvent: (Any) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable {
        TODO("Not yet implemented")
    }
}
package ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import ru.ilya.messenger.domain.entities.Result
import ru.ilya.messenger.domain.entities.StreamAllModel
import ru.ilya.messenger.domain.entities.StreamsWithTopicsEntityModel
import ru.ilya.messenger.domain.entities.TopicData
import ru.ilya.messenger.domain.useCases.GetAllStreamsListUseCase
import ru.ilya.messenger.domain.useCases.GetStreamTopicsUseCase
import ru.ilya.messenger.domain.useCases.dbUseCases.AddAllStreamWithTopicsInDbUseCase
import ru.ilya.messenger.domain.useCases.dbUseCases.GetAllStreamsFromDbUseCase
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsCommand
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.elm.models.AllStreamsEvent
import vivid.money.elmslie.core.disposable.Disposable
import vivid.money.elmslie.core.store.DefaultActor
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class AllStreamsActor @Inject constructor(
    private val getAllStreamsListUseCase: GetAllStreamsListUseCase,
    private val getStreamTopicsUseCase: GetStreamTopicsUseCase,
    private val getAllStreamsFromDbUseCase: GetAllStreamsFromDbUseCase,
    private val addAllStreamWithTopicsInDbUseCase: AddAllStreamWithTopicsInDbUseCase
) : Actor<AllStreamsCommand, AllStreamsEvent>, DefaultActor<Any, Any> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(command: AllStreamsCommand): Flow<AllStreamsEvent> = when (command) {

        is AllStreamsCommand.LoadStreamList -> {
            getAllStreamsListUseCase.invoke(auth = command.auth)
                .flatMapLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            // список стримов (без топиков внутри)
                            flowOf(AllStreamsEvent.Internal.StreamsLoaded(result.data))
                        }
                        is Result.Error -> {
                            flowOf(AllStreamsEvent.Internal.ErrorLoading(Exception(result.message)))
                        }
                        is Result.Loading -> {
                            flowOf(AllStreamsEvent.Internal.Loading)
                        }
                    }
                }
                .catch { e ->
                    emit(AllStreamsEvent.Internal.ErrorLoading(e))
                }
                .flowOn(Dispatchers.IO)
        }

        is AllStreamsCommand.LoadStreamBySearch -> {
            getAllStreamsListUseCase.invoke(auth = command.auth)
                .flatMapLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            // список стримов (без топиков внутри)
                            flowOf(
                                AllStreamsEvent.Internal.StreamsLoadedForSearch(
                                    query = command.query,
                                    result.data
                                )
                            )
                        }
                        is Result.Error -> {
                            flowOf(AllStreamsEvent.Internal.ErrorLoading(Exception(result.message)))
                        }
                        is Result.Loading -> {
                            flowOf(AllStreamsEvent.Internal.Loading)
                        }
                    }
                }
                .catch { e ->
                    emit(AllStreamsEvent.Internal.ErrorLoading(e))
                }
                .flowOn(Dispatchers.IO)
        }

        is AllStreamsCommand.LoadStreamTopicListForSearch -> {
            flow<AllStreamsEvent> {
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
                emit(AllStreamsEvent.Internal.StreamsWithTopicsLoadedForSearch(result))
            }.catch { e ->
                emit(AllStreamsEvent.Internal.ErrorLoading(e))
            }.flowOn(Dispatchers.IO)
        }

        is AllStreamsCommand.LoadStreamTopicList -> {
            flow<AllStreamsEvent> {
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
                emit(AllStreamsEvent.Internal.StreamsWithTopicsLoaded(result))
            }.catch { e ->
                emit(AllStreamsEvent.Internal.ErrorLoading(e))
            }.flowOn(Dispatchers.IO)
        }
        is AllStreamsCommand.LoadStreamListFromDb -> {
            flow<AllStreamsEvent> {
                val listStreamsWithTopicsEntityModel =
                    getAllStreamsFromDbUseCase.invoke(isSubscribed = command.isSubscribed)
                val arrayStreamsAllModel =
                    getListStreamAllModelFromListStreamsWithTopicsEntityModel(
                        listStreamsWithTopicsEntityModel
                    )
                emit(AllStreamsEvent.Internal.StreamsWithTopicsFromDbLoaded(arrayStreamsAllModel))
            }.catch { e ->
                emit(AllStreamsEvent.Internal.ErrorLoading(e))
            }.flowOn(Dispatchers.IO)
        }
        is AllStreamsCommand.AddStreamToDb -> {
            var idOfRow = 1
            flow<AllStreamsEvent> {
                val arrayStreamsWithTopics = ArrayList<StreamsWithTopicsEntityModel>()
                command.streamList.forEach { streamOfStreamData ->
                    // в StreamData есть свойство topicList: MutableList<TopicData>, преобразуем в модель
                    streamOfStreamData.topicList.forEach { topicData ->
                        val streamWithTopicModel = StreamsWithTopicsEntityModel(
                            id = idOfRow++,
                            streamId = streamOfStreamData.streamId,
                            streamName = streamOfStreamData.name,
                            isSubscribed = false,
                            topicName = topicData.name
                        )
                        arrayStreamsWithTopics.add(streamWithTopicModel)
                    }
                }
                addAllStreamWithTopicsInDbUseCase(listStreamsWithTopicsEntityModel = arrayStreamsWithTopics)
            }.catch {
            }.flowOn(Dispatchers.IO)
        }
    }

    private fun getListStreamAllModelFromListStreamsWithTopicsEntityModel(
        listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>
    ): List<StreamAllModel> {
        val streamDataList = listStreamsWithTopicsEntityModel.groupBy { it.streamId }
            .map { (streamId, group) ->
                StreamAllModel(
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
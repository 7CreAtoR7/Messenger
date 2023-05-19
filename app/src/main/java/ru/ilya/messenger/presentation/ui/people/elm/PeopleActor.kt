package ru.ilya.messenger.presentation.ui.people.elm


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import ru.ilya.messenger.domain.entities.Result
import ru.ilya.messenger.domain.useCases.LoadUserStatusUseCase
import ru.ilya.messenger.domain.useCases.LoadUsersUseCase
import ru.ilya.messenger.presentation.ui.people.elm.models.Command
import ru.ilya.messenger.presentation.ui.people.elm.models.Event
import vivid.money.elmslie.core.disposable.Disposable
import vivid.money.elmslie.core.store.DefaultActor
import vivid.money.elmslie.coroutines.Actor

class PeopleActor(
    private val loadUsersUseCase: LoadUsersUseCase,
    private val loadUserStatusUseCase: LoadUserStatusUseCase
) : Actor<Command, Event>, DefaultActor<Any, Any> {

    override fun execute(command: Command): Flow<Event> = when (command) {
        is Command.LoadUsersList -> {
            loadUsersUseCase.invoke(auth = command.auth)
                .map { result ->
                    when (result) {
                        is Result.Success -> {
                            // спсиок пользователей без статуса
                            Event.Internal.UsersLoaded(result.data)
                        }
                        is Result.Error -> {
                            Event.Internal.ErrorLoading(Exception(result.message))
                        }
                        is Result.Loading -> {
                            Event.Internal.Loading
                        }
                    }
                }
                .catch { e ->
                    emit(Event.Internal.ErrorLoading(e))
                }
                .flowOn(Dispatchers.IO)
        }
        is Command.LoadUserStatus -> {
            flow<Event> {
                val result = command.usersList.map { user ->
                    val status = coroutineScope {
                        async {
                            try {
                                loadUserStatusUseCase.invoke(
                                    auth = command.auth,
                                    userId = user.userId
                                )
                            } catch (e: Exception) {
                                Result.Error("My error in people actor")
                            }
                        }
                    }
                    user to status
                }.map {
                    val user = it.first
                    val statusResult = it.second.await()
                    if (statusResult is Result.Success) {
                        user.copy(status = statusResult.data.status)
                    } else {
                        user
                    }
                }
                emit(Event.Internal.UsersStatusLoaded(result))
            }.catch { e ->
                emit(Event.Internal.ErrorLoading(e))
            }.flowOn(Dispatchers.IO)
        }
    }

    override fun execute(
        command: Any,
        onEvent: (Any) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable {
        TODO("Not yet implemented")
    }
}
package ru.ilya.messenger.presentation.ui.people.elm

import okhttp3.Credentials
import ru.ilya.messenger.presentation.ui.people.elm.models.Command
import ru.ilya.messenger.presentation.ui.people.elm.models.Effect
import ru.ilya.messenger.presentation.ui.people.elm.models.Event
import ru.ilya.messenger.presentation.ui.people.elm.models.State
import ru.ilya.messenger.util.Constants
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class PeopleReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {
        is Event.Ui.LoadUsers -> {
            // старый список очищается при открытии фрагмента
            state { copy(isLoading = true, error = null, users = emptyList()) }
            commands {
                +Command.LoadUsersList(
                    Credentials.basic(
                        Constants.EMAIL,
                        Constants.PASSWORD
                    )
                )
            }
        }
        is Event.Internal.UsersLoaded -> {
            // отправляем команду с получением статуса для пользователей, которые есть в списке
            commands {
                +Command.LoadUserStatus(
                    auth = Credentials.basic(
                        Constants.EMAIL,
                        Constants.PASSWORD
                    ), event.items
                )
            }
        }
        is Event.Internal.UsersStatusLoaded -> {
            // приходит список пользователей, у которых уже есть статус
            state { copy(isLoading = false, users = event.usersListWithStatus) }
        }
        is Event.Internal.ErrorLoading -> {
            // в случае ошибки (как правило - удаленный аккаунт) показываем статус offline
        }
        is Event.Internal.Loading -> {}
    }
}
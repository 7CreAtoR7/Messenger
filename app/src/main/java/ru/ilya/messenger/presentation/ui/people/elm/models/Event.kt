package ru.ilya.messenger.presentation.ui.people.elm.models

import ru.ilya.messenger.domain.entities.UserPeople

sealed class Event {

    sealed class Ui : Event() {

        object LoadUsers : Ui()
    }

    // результат команды загрузки пользователей - сам список либо ошибка
    sealed class Internal : Event() {

        data class UsersLoaded(val items: List<UserPeople>) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()

        data class UsersStatusLoaded(
            val usersListWithStatus: List<UserPeople>
        ) : Internal()

        object Loading : Internal()

    }
}
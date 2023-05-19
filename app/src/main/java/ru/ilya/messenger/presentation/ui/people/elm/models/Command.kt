package ru.ilya.messenger.presentation.ui.people.elm.models

import ru.ilya.messenger.domain.entities.UserPeople

// асинхр операция загрузки пользователей
sealed class Command {

    data class LoadUsersList(val auth: String) : Command()

    data class LoadUserStatus(
        val auth: String,
        val usersList: List<UserPeople>
    ) : Command()
}

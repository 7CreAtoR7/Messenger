package ru.ilya.messenger.presentation.ui.people.elm.models

import ru.ilya.messenger.domain.entities.UserPeople

// на экране people 2 состояния:
// загрузка и отображение загруженного списка пользователей
data class State(
    val isLoading: Boolean = false,
    val users: List<UserPeople> = emptyList(),
    val error: Throwable? = null,
    val userStatus: Boolean = false
)

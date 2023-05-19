package ru.ilya.messenger.presentation.ui.people.elm.models

// одно ui событие - отображение snack-bar ошибки
sealed class Effect {

    data class UsersLoadError(val error: Throwable): Effect()

    data class UsersStatusLoadError(val error: Throwable): Effect()
}
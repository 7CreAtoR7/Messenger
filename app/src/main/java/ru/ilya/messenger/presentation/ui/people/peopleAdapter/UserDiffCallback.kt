package ru.ilya.messenger.presentation.ui.people.peopleAdapter

import androidx.recyclerview.widget.DiffUtil
import ru.ilya.messenger.domain.entities.UserPeople

class UserDiffCallback: DiffUtil.ItemCallback<UserPeople>() {
    override fun areItemsTheSame(oldItem: UserPeople, newItem: UserPeople): Boolean {
        return oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: UserPeople, newItem: UserPeople): Boolean {
        return oldItem == newItem
    }
}

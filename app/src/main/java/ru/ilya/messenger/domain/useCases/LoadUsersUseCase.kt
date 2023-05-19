package ru.ilya.messenger.domain.useCases


import kotlinx.coroutines.flow.Flow
import ru.ilya.messenger.domain.entities.UserPeople
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class LoadUsersUseCase @Inject constructor(
    private val messageStreamsRepository: MessengerRepository
) {

    operator fun invoke(auth: String): Flow<Result<List<UserPeople>>> {
        return messageStreamsRepository.loadUsers(auth)
    }

}
package ru.ilya.messenger.domain.useCases

import ru.ilya.messenger.domain.entities.AggregatedModel
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class LoadUserStatusUseCase @Inject constructor(
    private val messageRepository: MessengerRepository
) {

    suspend operator fun invoke(
        auth: String,
        userId: Int
    ): Result<AggregatedModel> {
        return messageRepository.loadUserStatus(auth = auth, userId = userId)
    }
}
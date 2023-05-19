package ru.ilya.messenger.domain.useCases

import ru.ilya.messenger.domain.entities.TopicData
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class GetStreamTopicsUseCase @Inject constructor(
    private val messageRepository: MessengerRepository
) {

    suspend operator fun invoke(auth: String, streamId: Int): Result<List<TopicData>> {
        return messageRepository.getTopicsInStreamById(auth, streamId)
    }
}
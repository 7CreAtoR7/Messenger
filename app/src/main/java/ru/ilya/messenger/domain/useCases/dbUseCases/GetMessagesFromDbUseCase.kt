package ru.ilya.messenger.domain.useCases.dbUseCases

import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class GetMessagesFromDbUseCase @Inject constructor(
    private val messageStreamsRepository: MessengerRepository
) {

    suspend operator fun invoke(streamId: Int): List<MessageModel> {
        return messageStreamsRepository.getMessagesFromDb(streamId)
    }
}
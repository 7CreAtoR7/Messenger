package ru.ilya.messenger.domain.useCases.dbUseCases

import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class AddMessagesInDbUseCase @Inject constructor(
    private val messageStreamsRepository: MessengerRepository
) {

    suspend operator fun invoke(listMessageModel: List<MessageModel>) {
        return messageStreamsRepository.addMessagesInDb(listMessageModel)
    }
}
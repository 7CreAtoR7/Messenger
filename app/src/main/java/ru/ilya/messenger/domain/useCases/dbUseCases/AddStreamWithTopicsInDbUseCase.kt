package ru.ilya.messenger.domain.useCases.dbUseCases

import ru.ilya.messenger.domain.entities.StreamsWithTopicsEntityModel
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class AddStreamWithTopicsInDbUseCase @Inject constructor(
    private val messageStreamsRepository: MessengerRepository
) {

    suspend operator fun invoke(listStreamsWithTopicsEntityModel: List<StreamsWithTopicsEntityModel>) {
        return messageStreamsRepository.addSubscribedStreamWithTopicsInDb(
            listStreamsWithTopicsEntityModel
        )
    }
}
package ru.ilya.messenger.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.ilya.messenger.domain.entities.StreamAllModel
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class GetAllStreamsListUseCase @Inject constructor(
    private val messageStreamsRepository: MessengerRepository
) {

    operator fun invoke(auth: String): Flow<Result<List<StreamAllModel>>> {
        return messageStreamsRepository.getAllStreams(auth)
    }
}
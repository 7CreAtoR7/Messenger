package ru.ilya.messenger.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.ilya.messenger.domain.entities.StreamData
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject
import ru.ilya.messenger.domain.entities.Result
class GetSubscribedStreamsListUseCase @Inject constructor(
    private val messageStreamsRepository: MessengerRepository
) {

    operator fun invoke(auth: String): Flow<Result<List<StreamData>>> {
        return messageStreamsRepository.getSubscribedStreams(auth)
    }
}
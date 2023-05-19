package ru.ilya.messenger.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class GetTopicMessagesUseCase @Inject constructor(
    private val messengerRepository: MessengerRepository
) {

    operator fun invoke(
        auth: String,
        numBefore: Int,
        numAfter: Int,
        anchor: String,
        narrow: String
    ): Flow<Result<List<MessageModel>>> {
        return messengerRepository.getTopicMessages(
            auth = auth,
            numBefore = numBefore,
            numAfter = numAfter,
            anchor = anchor,
            narrow = narrow
        )
    }
}
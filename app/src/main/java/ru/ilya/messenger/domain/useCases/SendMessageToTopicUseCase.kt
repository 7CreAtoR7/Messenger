package ru.ilya.messenger.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.ilya.messenger.domain.entities.SendMessageResponseModel
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject
import ru.ilya.messenger.domain.entities.Result
class SendMessageToTopicUseCase @Inject constructor(
    private val messengerRepository: MessengerRepository
) {

    operator fun invoke(
        auth: String,
        type: String,
        content: String,
        to: String,
        topic: String
    ): Flow<Result<SendMessageResponseModel>> {
        return messengerRepository.sendMessageInTopic(
            auth = auth,
            type = type,
            content = content,
            to = to,
            topic = topic
        )
    }

}
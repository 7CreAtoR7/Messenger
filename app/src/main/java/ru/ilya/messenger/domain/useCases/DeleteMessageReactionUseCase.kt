package ru.ilya.messenger.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.ilya.messenger.data.network.messagesDTO.DeleteReactionResponseDto
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject
import ru.ilya.messenger.domain.entities.Result
class DeleteMessageReactionUseCase @Inject constructor(
    private val messageStreamsRepository: MessengerRepository
) {

    operator fun invoke(
        auth: String,
        messageId: Int,
        emojiName: String
    ): Flow<Result<DeleteReactionResponseDto>> {
        return messageStreamsRepository.deleteMessageReaction(
            auth, messageId, emojiName
        )
    }


}
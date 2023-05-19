package ru.ilya.messenger.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.ilya.messenger.data.network.messagesDTO.AddReactionResponseDto
import ru.ilya.messenger.domain.repository.MessengerRepository
import javax.inject.Inject

class AddMessageReactionUseCase @Inject constructor(
    private val messageStreamsRepository: MessengerRepository
) {

    operator fun invoke(
        auth: String,
        messageId: Int,
        emojiName: String
    ): Flow<Result<AddReactionResponseDto>> {
        return messageStreamsRepository.addMessageReaction(
            auth, messageId, emojiName
        )
    }

}
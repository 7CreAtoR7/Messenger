package ru.ilya.messenger.data.network.streamsDTO

data class SubscribedStreamsResponseDTO(
    val result: String? = null,
    val subscriptions: List<SubscribedStreamDto> = emptyList()
)

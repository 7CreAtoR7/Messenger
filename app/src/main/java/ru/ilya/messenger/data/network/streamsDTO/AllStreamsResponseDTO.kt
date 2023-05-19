package ru.ilya.messenger.data.network.streamsDTO

data class AllStreamsResponseDTO(
    val result: String? = null,
    val streams: List<AllStreamDto> = emptyList()
)

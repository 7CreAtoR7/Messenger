package ru.ilya.messenger.data.network.topicsDTO

data class AllTopicsResponseDTO(
    val result: String? = null,
    val topics: List<TopicResponseDto> = emptyList()
)

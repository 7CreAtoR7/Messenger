package ru.ilya.messenger.presentation.ui.channels.streamsFragments.searching

import ru.ilya.messenger.domain.entities.StreamAllModel

sealed interface SearchScreenState {

    object Error : SearchScreenState

    object Loading : SearchScreenState

    class Data(val list: List<StreamAllModel>) : SearchScreenState

    object Init : SearchScreenState
}

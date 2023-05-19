package ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.pagination


class PaginationAdapterHelper(
    private val onLoadMoreCallback: (offset: Int) -> Unit
) {

    fun onBind(adapterPosition: Int) {
        if (adapterPosition == DEFAULT_LOAD_MORE_MESSAGES) {
            onLoadMoreCallback(adapterPosition)
        }
    }

    companion object {
        private const val DEFAULT_LOAD_MORE_MESSAGES = 1
    }
}

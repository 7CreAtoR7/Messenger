package ru.ilya.messenger.presentation.ui.chat.chatAdapter.myMessage


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ilya.messenger.databinding.MyMessageItemBinding
import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.AdapterDelegate
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.DelegateItem
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.pagination.PaginationAdapterHelper

class MyMessageDelegate(
    private val paginationAdapterHelper: PaginationAdapterHelper
) : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            MyMessageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int,
        itemCount: Int
    ) {
        (holder as ViewHolder).bind(item.content() as MessageModel)
        paginationAdapterHelper.onBind(position)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is MyMessageDelegateItem

    class ViewHolder(private val binding: MyMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MessageModel) {
            with(binding) {
                setupUi(model)
            }
        }

        private fun MyMessageItemBinding.setupUi(message: MessageModel) {
            sentMessage.text = message.content

        }
    }

}

package ru.ilya.messenger.presentation.ui.chat.chatAdapter.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ilya.messenger.databinding.DateItemBinding
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.AdapterDelegate
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.DelegateItem

class DateDelegate : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            DateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int,
        itemCount: Int
    ) {
        (holder as ViewHolder).bind(item.content() as DateModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is DateDelegateItem

    class ViewHolder(private val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: DateModel) {
            with(binding) {
                date.text = model.date
            }
        }
    }
}

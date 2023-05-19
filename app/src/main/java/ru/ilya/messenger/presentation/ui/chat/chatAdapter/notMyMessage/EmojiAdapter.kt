package ru.ilya.messenger.presentation.ui.chat.chatAdapter.notMyMessage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ilya.messenger.R
import ru.ilya.messenger.domain.emoji.EmojiNCU

class EmojiAdapter(
    private var emojiList: List<EmojiNCU>
) : RecyclerView.Adapter<EmojiAdapter.ViewHolder>() {

    var onMessageItemClickListener: ((EmojiNCU) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.emoji_item, parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = emojiList[position]
        val stickerString = String(Character.toChars(item.code))
        holder.item.text = stickerString
        holder.item.setOnClickListener {
            onMessageItemClickListener?.invoke(item)
        }
    }

    override fun getItemCount(): Int = emojiList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: TextView = itemView.findViewById(R.id.emoji)
    }
}
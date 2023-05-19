package ru.ilya.messenger.presentation.ui.chat.chatAdapter.notMyMessage

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.ilya.messenger.R
import ru.ilya.messenger.databinding.NotMyMessageItemBinding
import ru.ilya.messenger.domain.emoji.EmojiLib
import ru.ilya.messenger.domain.entities.MessageModel
import ru.ilya.messenger.presentation.customViewsEmojis.FlexBoxLayout
import ru.ilya.messenger.presentation.ui.chat.ClickReaction
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.AdapterDelegate
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.DelegateItem
import ru.ilya.messenger.presentation.ui.chat.chatAdapter.utils.pagination.PaginationAdapterHelper

class NotMyMessageDelegate(
    private val clickReactionAdd: ClickReaction,
    private val paginationAdapterHelper: PaginationAdapterHelper
) : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            NotMyMessageItemBinding.inflate(
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
        (holder as ViewHolder).bind(item.content() as MessageModel, clickReactionAdd)
        paginationAdapterHelper.onBind(position)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is NotMyMessageDelegateItem

    class ViewHolder(private val binding: NotMyMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var dialog: BottomSheetDialog
        private lateinit var emojiAdapter: EmojiAdapter
        private lateinit var recyclerView: RecyclerView
        private lateinit var friendEmojis: FlexBoxLayout
        private lateinit var clickReactionAddInside: ClickReaction

        fun bind(model: MessageModel, clickReactionAdd: ClickReaction) {
            clickReactionAddInside = clickReactionAdd
            with(binding) {
                setupUi(model)
            }
        }

        private fun NotMyMessageItemBinding.setupUi(message: MessageModel) {
            val friendMessage =
                receivedMessage.findViewById<TextView>(R.id.user_messageText_flexBox)

            friendMessage.text = message.content

            val friendName = receivedMessage.findViewById<TextView>(R.id.user_name_flexBox)
            friendName.text = message.senderFullName

            val friendImage = receivedMessage.findViewById<ImageView>(R.id.user_image_flexBox)

            friendEmojis = blockReceivedMessage.findViewById(R.id.emojisFlexBox)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.circle_bg)

            Glide.with(friendName.context)
                .load(message.avatarUrl)
                .centerCrop()
                .circleCrop()
                .apply(requestOptions)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        friendImage.background = AppCompatResources.getDrawable(
                            friendImage.context,
                            R.drawable.circle_bg
                        )
                        friendImage.setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Очищаем ImageView если изображение не загружено
                        friendImage.background = null
                        friendImage.setImageDrawable(placeholder)
                    }
                })

            friendEmojis.addReactions(message.reactions)
            friendEmojis.setEmojiOnClickListener(clickReactionAddInside, message.id)

            val plus = friendEmojis.findViewById<ImageView>(R.id.image_view_of_plus)
            if (plus != null) {
                friendEmojis.setPlusClickListener(plus) {
                    // показ BottomSheet из делегата
                    showBottomSheet(message.id)
                }
            }
            blockReceivedMessage.setOnLongClickListener {
                showBottomSheet(message.id)
                true
            }
        }


        @SuppressLint("InflateParams")
        fun showBottomSheet(messageId: Int) {
            val dialogView = LayoutInflater.from(itemView.context)
                .inflate(R.layout.bottom_sheet, null)
            dialog = BottomSheetDialog(itemView.context, R.style.BottomSheetDialogTheme)
            dialog.setContentView(dialogView)

            recyclerView = dialogView.findViewById(R.id.rvEmoji)
            with(recyclerView) {
                layoutManager = GridLayoutManager(itemView.context, COLUMNS_COUNT)
                emojiAdapter = EmojiAdapter(EmojiLib.emojiSetNCU)
                adapter = emojiAdapter
            }

            dialog.show()
            initEmojiInDialogClickListener(messageId)
        }

        private fun initEmojiInDialogClickListener(messageId: Int) {
            emojiAdapter.onMessageItemClickListener = { chosenEmoji ->
                dialog.hide()
                //  обработка клика в MessageChatFragment - метод addMessageReaction
                clickReactionAddInside.onEmojiInBottomSheetClick(messageId, chosenEmoji.name)
            }
        }
    }


    companion object {
        const val COLUMNS_COUNT = 5
    }


}

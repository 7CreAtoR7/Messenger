package ru.ilya.messenger.presentation.customViewsEmojis

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.*
import ru.ilya.messenger.R
import ru.ilya.messenger.domain.emoji.EmojiNCU
import ru.ilya.messenger.domain.entities.Reactions
import ru.ilya.messenger.presentation.ui.chat.ClickReaction

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    fun setPlusClickListener(imageView: ImageView, listener: OnClickListener) {
        imageView.setOnClickListener(listener)
    }

    private var maxLengthForLayout = 0

    fun setEmojiOnClickListener(clickReactionAddInside: ClickReaction, messageId: Int) {
        children.forEach { reaction ->
            reaction.setOnClickListener {
                if (reaction is ImageView) { // кнопка +
                    // В NotMyMessageDelegate у imageview вызываю setPlusClickListener
                } else if (reaction is ReactionView) {
                    // если мы ранее уже нажимали на этот эмодзи
                    clickReactionAddInside.onEmojiClicked(it, messageId)
                }
            }
        }
    }

    fun addReactions(listReactions: List<Reactions>) {
        removeAllViews()
        val mapEmojiCount = hashMapOf<String, Int>()

        listReactions.forEach { reaction ->
            // создаем реакцию и добавляем как child внутрь flexBox
            val reactionView = ReactionView(context)

            val layoutParams =
                MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            layoutParams.leftMargin = 10
            layoutParams.topMargin = 10
            reactionView.layoutParams = layoutParams

            reactionView.setReaction(EmojiNCU.getCodeString(reaction.emoji_code))

            var countCurrentEmoji = 0
            // у нас список реакций, смотрим для каждого стикера есть ли отправитель такого же
            for (innerReaction in listReactions) {
                if (innerReaction.emoji_code == reaction.emoji_code) {
                    countCurrentEmoji++
                    if (innerReaction.user_id == MY_USER_ID)
                        reactionView.setSelected()
                }
            }
            if (reaction.user_id == MY_USER_ID)
                reactionView.setSelected()
            if (!mapEmojiCount.containsKey(reaction.emoji_code)) {
                mapEmojiCount[reaction.emoji_code] = countCurrentEmoji
                reactionView.setReactionCount(countCurrentEmoji)
                reactionView.setNameEmoji(reaction.emoji_name)
                addView(reactionView, START_INDEX)
            }
        }

        // imageview плюсика добавляется, если есть как минимум одна реакция
        if (listReactions.isNotEmpty()) {
            val plusImageView = ImageView(context).apply {
                id = R.id.image_view_of_plus
                setImageResource(R.drawable.ic_baseline_add_24)
            }
            addView(plusImageView)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var totalHeight = 0
        var totalWidth = 0
        var currentRowWidth = 0

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        maxLengthForLayout = parentWidth

        val index = if (childCount == 1) childCount - 1 else childCount

        for (i in 0 until index) {
            if (i == START_INDEX)
                totalHeight =
                    getChildAt(START_INDEX).measuredHeight + getChildAt(i).marginTop + getChildAt(i).marginBottom + getChildAt(
                        i
                    ).paddingTop + getChildAt(i).paddingBottom
            if (totalWidth + getChildAt(i).measuredWidth + getChildAt(i).marginLeft + getChildAt(i).marginRight + getChildAt(
                    i
                ).paddingLeft + getChildAt(i).paddingRight >= parentWidth && currentRowWidth + getChildAt(
                    i
                ).measuredWidth + getChildAt(i).marginLeft + getChildAt(i).marginRight + getChildAt(
                    i
                ).paddingLeft + getChildAt(i).paddingRight >= parentWidth
            ) {
                // переходим на новую строку, увеличивая высоту
                totalHeight += getChildAt(i).measuredHeight + getChildAt(i).marginTop + getChildAt(i).marginBottom + getChildAt(
                    i
                ).paddingTop + getChildAt(i).paddingBottom
                currentRowWidth =
                    getChildAt(i).measuredWidth + getChildAt(i).marginLeft + getChildAt(i).marginRight + getChildAt(
                        i
                    ).paddingRight + getChildAt(i).paddingLeft
            } else {
                currentRowWidth += getChildAt(i).measuredWidth + getChildAt(i).marginLeft + getChildAt(
                    i
                ).marginRight + getChildAt(i).paddingLeft + getChildAt(i).paddingRight
                if (maxLengthForLayout - totalWidth < parentWidth / childCount) { // parentWidth / childCount = средний размер 1 реакции
                    totalWidth = maxLengthForLayout
                } else {
                    totalWidth += getChildAt(i).measuredWidth + getChildAt(i).marginLeft + getChildAt(
                        i
                    ).marginRight + getChildAt(i).paddingRight + getChildAt(i).paddingLeft
                }

            }

            measureChildWithMargins(
                getChildAt(i),
                widthMeasureSpec,
                getChildAt(i).measuredWidth + getChildAt(i).marginLeft + getChildAt(i).marginRight + getChildAt(
                    i
                ).paddingLeft + getChildAt(i).paddingRight,
                heightMeasureSpec,
                getChildAt(i).measuredHeight + getChildAt(i).marginTop + getChildAt(i).marginBottom + getChildAt(
                    i
                ).paddingTop + getChildAt(i).paddingBottom
            )

        }
        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = 0
        var offsetY = 0

        for (i in 0 until childCount) {
            val currentReactionChild = getChildAt(i)

            if (offsetX + currentReactionChild.measuredWidth + currentReactionChild.marginLeft + currentReactionChild.marginRight + currentReactionChild.paddingRight + currentReactionChild.paddingRight >= maxLengthForLayout
            ) {
                offsetY += currentReactionChild.measuredHeight + currentReactionChild.marginTop + currentReactionChild.marginBottom + currentReactionChild.paddingTop + currentReactionChild.paddingBottom
                offsetX = paddingLeft
            }

            currentReactionChild.layout(
                offsetX,
                offsetY,
                offsetX + currentReactionChild.measuredWidth + currentReactionChild.marginLeft + currentReactionChild.marginRight,
                offsetY + currentReactionChild.measuredHeight + currentReactionChild.marginTop + currentReactionChild.marginBottom
            )
            offsetX += currentReactionChild.measuredWidth + currentReactionChild.marginLeft + currentReactionChild.marginRight + currentReactionChild.paddingLeft + currentReactionChild.paddingRight
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    companion object {

        const val START_INDEX = 0
        const val MY_USER_ID = 604430
    }

}
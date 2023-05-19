package ru.ilya.messenger.presentation.customViewsEmojis

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import ru.ilya.messenger.R

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private var image: View
    var name: View
    private var textMessage: View

    init {
        inflate(context, R.layout.custom_view_group_content, this)

        image = findViewById(R.id.user_image_flexBox)
        name = findViewById(R.id.user_name_flexBox)
        textMessage = findViewById(R.id.user_messageText_flexBox)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(image, widthMeasureSpec, 0, heightMeasureSpec, 0)

        measureChildWithMargins(
            name,
            widthMeasureSpec,
            image.measuredWidth + image.marginLeft + image.marginRight,
            heightMeasureSpec,
            image.measuredHeight + image.marginTop + image.marginBottom
        )

        measureChildWithMargins(
            textMessage,
            widthMeasureSpec,
            name.measuredWidth + name.marginLeft + name.marginRight,
            heightMeasureSpec,
            name.measuredHeight + name.marginTop + name.marginBottom
        )

        val totalWidth = paddingLeft + paddingRight +
                image.measuredWidth + +image.marginLeft + image.marginRight +
                maxOf(
                    name.measuredWidth + name.marginLeft + name.marginRight,
                    textMessage.marginLeft + textMessage.marginRight + textMessage.measuredWidth
                )

        val totalHeight = paddingTop + paddingBottom +
                name.measuredHeight + textMessage.measuredHeight

        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft + image.marginLeft
        val offsetY = paddingTop
        image.layout(
            offsetX,
            offsetY + image.marginTop,
            offsetX + image.measuredWidth,
            offsetY + image.measuredHeight + image.marginTop
        )

        offsetX += image.measuredWidth + image.marginRight

        name.layout(
            offsetX,
            offsetY + name.marginTop,
            offsetX + name.measuredWidth,
            offsetY + name.marginTop + name.measuredHeight
        )

        textMessage.layout(
            offsetX,
            offsetY + name.marginTop + name.measuredHeight + name.marginBottom + textMessage.marginTop,
            offsetX + textMessage.measuredWidth + textMessage.marginLeft + textMessage.marginRight,
            offsetY + name.marginTop + name.measuredHeight + name.marginBottom +
                    textMessage.marginTop + textMessage.measuredHeight + textMessage.marginBottom
        )
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

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

}
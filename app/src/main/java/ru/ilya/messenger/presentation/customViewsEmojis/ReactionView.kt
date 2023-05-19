package ru.ilya.messenger.presentation.customViewsEmojis


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import ru.ilya.messenger.R


class ReactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var nameOfEmoji = ""

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 20f.sp(context)
        color = Color.WHITE
        setPadding(7, 7, 7, 7)
    }

    private val textBounds = Rect()

    private var textToDraw = ""

    init {
        context.withStyledAttributes(attrs, R.styleable.ReactionView) {
            val reaction = this.getString(R.styleable.ReactionView_reaction)
            val count = this.getInt(R.styleable.ReactionView_count, DEF_VALUE)
            textToDraw = "$reaction $count"
        }
        setBackgroundResource(R.drawable.bg_reaction_view)
        setOnClickListener {
            it.isSelected = !it.isSelected
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, DEF_VALUE, textToDraw.length, textBounds)

        val textWidth = textBounds.width()
        val textHeight = textBounds.height()

        val measureWidth = resolveSize(
            textWidth + paddingLeft + paddingRight + marginRight +
                    marginLeft, widthMeasureSpec
        )
        val measureHeight = resolveSize(
            textHeight + paddingTop + paddingBottom + marginTop +
                    marginBottom, heightMeasureSpec
        )

        setMeasuredDimension(measureWidth, measureHeight)
    }


    override fun onDraw(canvas: Canvas) {
        val centerY = height / DENOMINATOR - textBounds.exactCenterY()
        canvas.drawText(textToDraw, paddingLeft.toFloat(), centerY, textPaint)
    }

    private fun Float.sp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )

    fun setReaction(reaction: String) {
        textToDraw = reaction
    }

    fun setReactionCount(count: Int) {
        textToDraw = "$textToDraw $count"
    }

    fun setNameEmoji(name: String) {
        nameOfEmoji = name
    }

    fun setSelected() {
        isSelected = true
    }

    companion object {

        private const val DEF_VALUE = 0
        private const val DENOMINATOR = 2
    }
}
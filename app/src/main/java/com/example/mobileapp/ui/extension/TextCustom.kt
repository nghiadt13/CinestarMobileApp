package com.example.mobileapp.ui.extension

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt

fun TextView.setClickablePart(
    fullText: String,
    clickableText: String,
    @ColorInt normalColor: Int = currentTextColor,
    @ColorInt clickableColor: Int = Color.BLUE,
    isBoldClickable: Boolean = true,
    onClick: () -> Unit
) {
    val start = fullText.indexOf(clickableText)
    require(start >= 0) { "clickableText không nằm trong fullText" }
    val end = start + clickableText.length
 
    val ss = SpannableString(fullText)
    ss.setSpan(ForegroundColorSpan(normalColor), 0, fullText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) = onClick()
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isFakeBoldText = isBoldClickable
            ds.color = clickableColor
        }
    }
    ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    text = ss
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}

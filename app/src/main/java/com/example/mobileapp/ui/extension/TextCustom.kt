package com.example.mobileapp.ui.extension

import android.graphics.Color
import android.text.Spannable
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
    normalColor: Int,
    clickableColor: Int,
    isBoldClickable: Boolean = false,
    onClick: () -> Unit
) {
    val spannableString = SpannableString(fullText)
    val startIndex = fullText.indexOf(clickableText)

    if (startIndex != -1) {
        val endIndex = startIndex + clickableText.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) = onClick()
            override fun updateDrawState(ds: TextPaint) {
                ds.color = clickableColor
                ds.isFakeBoldText = isBoldClickable
                ds.isUnderlineText = false

            }
        }

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


    this.setTextColor(normalColor)
    this.text = spannableString
    this.movementMethod = LinkMovementMethod.getInstance()
}


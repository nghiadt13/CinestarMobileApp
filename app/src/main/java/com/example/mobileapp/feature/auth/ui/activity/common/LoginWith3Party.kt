package com.example.mobileapp.feature.auth.ui.activity.common

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivityLoginBinding

class LoginWith3Party : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý sự kiện click button Sign in with password --> redirect to LoginCommon
        binding.buttonViewSignin.setOnClickListener {
            val intent = Intent(this, LoginCommon::class.java)
            startActivity(intent)
        }

        // Setup clickable "Sign up" text
        setupSignUpText()
    }

    private fun setupSignUpText() {
        val fullText = "Don't have an account ? Sign up"
        val spannableString = SpannableString(fullText)
        val signUpText = "Sign up"
        val startIndex = fullText.indexOf(signUpText)
        val endIndex = startIndex + signUpText.length

        // Đổi màu cho "Sign up"
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.black))
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Thêm click listener cho "Sign up"
        val clickableSpan =
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        // TODO: Thay SignUpActivity bằng tên Activity đăng ký của bạn
                        val intent = Intent(this@LoginWith3Party, SignUp::class.java)
                        startActivity(intent)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isFakeBoldText = true
                        ds.color = ContextCompat.getColor(this@LoginWith3Party, R.color.orange)
                    }
                }
        spannableString.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.textViewSignup.text = spannableString
        binding.textViewSignup.movementMethod = LinkMovementMethod.getInstance()
        binding.textViewSignup.highlightColor = Color.TRANSPARENT // Bỏ highlight khi click
    }
}

package com.example.mobileapp.feature.auth.ui.activity.common

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivitySignUpCommonBinding
import com.example.mobileapp.ui.extension.setClickablePart

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpCommonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivitySignUpCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewSignIn.setClickablePart(
            fullText = "Already have an account ? Sign in",
            clickableText = "Sign in",
            normalColor = ContextCompat.getColor(this, R.color.black),
            clickableColor = ContextCompat.getColor(this, R.color.orange),
            isBoldClickable = true
        ) {
            startActivity(Intent(this, SignUp::class.java))
        }

        binding.textViewTerm.setClickablePart(
            fullText = "I agree to CinesStar Terms & Conditions",
            clickableText = "Terms & Conditions",
            normalColor = ContextCompat.getColor(this, R.color.black),
            clickableColor = ContextCompat.getColor(this, R.color.orange),
            isBoldClickable = true
        ) {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}
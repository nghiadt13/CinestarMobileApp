package com.example.mobileapp.feature.auth.ui.activity.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
}

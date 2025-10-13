package com.example.mobileapp.feature.homepage.ui.activity.common

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databinding.ActivityHomepageBinding

class HomePage : AppCompatActivity() {
    private lateinit var binding : ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.example.mobileapp.feature.homepage.ui.activity.common

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databinding.ActivityHomepageBinding
import com.example.mobileapp.feature.homepage.ui.fragment.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("HomePageActivity", "onCreate called")
        
        setupToolbar()
        setupFragment(savedInstanceState)
    }

    private fun setupToolbar() {
        binding.imageViewAvatar.setOnClickListener {
            Toast.makeText(this, "Avatar clicked", Toast.LENGTH_SHORT).show()
            Log.d("HomePageActivity", "Avatar clicked")
        }

        binding.imageButtonMyTicket.setOnClickListener {
            Toast.makeText(this, "My Tickets", Toast.LENGTH_SHORT).show()
            Log.d("HomePageActivity", "My Tickets clicked")
        }

        binding.imageButtonSetting.setOnClickListener {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            Log.d("HomePageActivity", "Settings clicked")
        }
    }

    private fun setupFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, HomeFragment())
                .commit()
            Log.d("HomePageActivity", "HomeFragment loaded")
        }
    }
}

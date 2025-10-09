package com.example.mobileapp.feature.auth.ui.activity.common

import android.adservices.ondevicepersonalization.InferenceInput
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databinding.ActivityCompleteProfileBinding
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Locale

class CompleteProfile : AppCompatActivity() {

    lateinit var binding : ActivityCompleteProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

    binding.inputFieldDateOfBirth.setOnClickListener {
        showDatePickerDialog(binding.inputFieldDateOfBirth)
    }

        binding.buttonViewBackButton.setOnClickListener { finish() }
    }

    private fun showDatePickerDialog(dateInput: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog  = DatePickerDialog(this, {
                _,selectedYear, selectedMonth,selectedDayOfMonth ->
            val selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDayOfMonth, selectedMonth + 1, selectedYear)
            dateInput.setText(selectedDate)
        }
            ,year
            ,month
            ,day
        )
        datePickerDialog.show()
    }

}
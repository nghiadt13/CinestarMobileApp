package com.example.mobileapp.feature.auth.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobileapp.databinding.FragmentCompleteProfileBinding
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Locale

class CompleteProfileFragment : Fragment() {
    private var _binding: FragmentCompleteProfileBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.inputFieldDateOfBirth.setOnClickListener {
            showDatePickerDialog(binding.inputFieldDateOfBirth)
        }

        binding.buttonViewBackButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun showDatePickerDialog(dateInput: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
                DatePickerDialog(
                        requireContext(),
                        { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                            val selectedDate =
                                    String.format(
                                            Locale.getDefault(),
                                            "%02d/%02d/%d",
                                            selectedDayOfMonth,
                                            selectedMonth + 1,
                                            selectedYear
                                    )
                            dateInput.setText(selectedDate)
                        },
                        year,
                        month,
                        day
                )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

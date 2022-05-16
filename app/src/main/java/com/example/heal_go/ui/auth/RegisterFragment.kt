package com.example.heal_go.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentRegisterBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.wajahatkarim3.easyvalidation.core.Validator
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController by lazy {
        findNavController()
    }

    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        initView()

        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.format(it)
            binding.etDate.setText(date)
        }

        binding.etDate.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                fragmentManager?.let { it1 -> datePicker.show(it1, "tag") }
            }
        }

        binding.registerBtn.setOnClickListener {
            if (validation()) {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginBtn.setOnClickListener {
            navController.navigate(R.id.registerFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun validation(): Boolean {

        val etEmail = binding.etEmail.text
        val etFullname = binding.etFullname.text
        val etPassword = binding.etPassword.text

        val validatiorEmail = Validator(etEmail.toString())
            .nonEmpty()
            .validEmail()
            .addErrorCallback {
                binding.etEmail.error = "Email is not valid!"
            }
            .addSuccessCallback {
                binding.etEmail.error = null
            }
            .check()


        val validatiorFullname = Validator(etFullname.toString())
            .nonEmpty()
            .noNumbers()
            .noSpecialCharacters()
            .addErrorCallback {
                binding.etFullname.error = "Name is not valid!"
            }
            .addSuccessCallback {
                binding.etFullname.error = null
            }
            .check()


        val validatorpassword = Validator(etPassword.toString())
            .nonEmpty()
            .minLength(8)
            .atleastOneNumber()
            .addErrorCallback {
                binding.etPassword.error = "Password is not valid!"
            }
            .addSuccessCallback {
                binding.etPassword.error = null
            }
            .check()

        return validatiorEmail && validatiorFullname && validatorpassword

    }

    private fun initView() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        calendar[Calendar.YEAR] = 2001
        calendar[Calendar.MONTH] = 2
        val openDate = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar[Calendar.YEAR] = 1960
        val startDate = calendar.timeInMillis

        val constraints = CalendarConstraints.Builder()
            .setOpenAt(openDate)
            .setStart(startDate)
            .setEnd(today)
            .build()

        datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(constraints)
                .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
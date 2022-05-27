package com.example.heal_go.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentRegisterBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.auth.viewmodel.AuthViewModel
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

    private val authViewModel by viewModels<AuthViewModel> { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
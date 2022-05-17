package com.example.heal_go.ui.auth

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentLoginBinding
import com.example.heal_go.databinding.FragmentWelcomeBinding
import com.example.heal_go.ui.auth.viewmodel.AuthViewModel
import com.wajahatkarim3.easyvalidation.core.Validator

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupView()

        binding.loginBtn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val emailValidator = Validator(email)
                .nonEmpty()
                .validEmail()
                .check()

            val passwordValidator = Validator(password)
                .nonEmpty()
                .minLength(8)
                .atleastOneNumber()
                .check()

            if (emailValidator && passwordValidator) {
                authViewModel.userLoginHandler(email, password)
                Toast.makeText(activity, "Data received", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.loginFragment_to_homeFragment)
            } else {
                Toast.makeText(activity, "Please check on your credentials", Toast.LENGTH_SHORT).show()
            }

        }

        binding.registerBtn.setOnClickListener {
            navController.navigate(R.id.loginFragment_to_registerFragment)
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.moveTaskToBack(true)
            activity?.finish()
        }


        return binding.root
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
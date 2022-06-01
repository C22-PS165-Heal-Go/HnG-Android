package com.example.heal_go.ui.auth

import android.content.Intent
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
import com.example.heal_go.data.network.response.UserSession
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentLoginBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.auth.viewmodel.AuthViewModel
import com.example.heal_go.ui.dashboard.DashboardActivity
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory
import com.example.heal_go.util.Status
import com.wajahatkarim3.easyvalidation.core.Validator


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>{ ViewModelFactory(requireContext()) }

    private val onBoardingViewModel by viewModels<OnboardingViewModel> {
        OnboardingViewModelFactory(OnboardingRepository(requireContext()))
    }

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

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.moveTaskToBack(true)
            activity?.finish()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val emailValidator = Validator(email).apply {
                nonEmpty()
                validEmail()
            }.check()

            val passwordValidator = Validator(password).apply {
                nonEmpty()
                minLength(8)
                atleastOneNumber()
            }.check()

            if (emailValidator && passwordValidator) {
                authViewModel.userLoginHandler(email, password)
            } else {
                Toast.makeText(activity, "Please check on your credentials", Toast.LENGTH_SHORT).show()
            }

            authViewModel.login.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Status.Loading -> {}
                    is Status.Success -> {
                        if (result.data?.code != null) {
                            Toast.makeText(activity, result.data?.message, Toast.LENGTH_SHORT).show()
                        }
                        else {
                            if (result.data?.success == true) {
                                Toast.makeText(activity, "Login Successful!", Toast.LENGTH_SHORT).show()

                                onBoardingViewModel.createLoginSession(UserSession(true, result.data))
                                val intent = Intent(activity, DashboardActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }
                    }
                    is Status.Error -> {
                        Toast.makeText(activity, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.registerBtn.setOnClickListener {
            navController.navigate(R.id.loginFragment_to_registerFragment)
        }
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
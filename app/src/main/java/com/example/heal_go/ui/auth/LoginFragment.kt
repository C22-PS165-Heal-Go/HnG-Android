package com.example.heal_go.ui.auth

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.heal_go.R
import com.example.heal_go.data.network.response.UserSession
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentLoginBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.auth.viewmodel.AuthViewModel
import com.example.heal_go.ui.dashboard.DashboardActivity
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory
import com.example.heal_go.util.LoadingDialog
import com.example.heal_go.util.Status
import com.wajahatkarim3.easyvalidation.core.Validator


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    /* build loading dialog */
    private lateinit var loadingDialogBuilder: LoadingDialog
    private lateinit var loadingDialog: AlertDialog

    private val authViewModel by viewModels<AuthViewModel> { ViewModelFactory(requireContext()) }

    private val onBoardingViewModel by viewModels<OnboardingViewModel> {
        OnboardingViewModelFactory(OnboardingRepository(requireContext()))
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    lateinit var submitDialogBuilder: AlertDialog.Builder

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
                Toast.makeText(
                    activity,
                    requireContext().getString(R.string.credential_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            authViewModel.login.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Status.Loading -> {
                        loadingDialog.show()
                    }
                    is Status.Success -> {
                        loadingDialog.dismiss()
                        if (result.data?.code != null) {
                            showSubmitDialog(false, null)
                        } else {
                            if (result.data?.success == true) {
                                onBoardingViewModel.createLoginSession(
                                    UserSession(
                                        true,
                                        result.data
                                    )
                                )
                                showSubmitDialog(true, null)
                            }
                        }
                    }
                    is Status.Error -> {
                        loadingDialog.dismiss()
                        showSubmitDialog(false, result.error)
                    }
                }
            }
        }

        binding.registerBtn.setOnClickListener {
            navController.navigate(R.id.loginFragment_to_registerFragment)
        }
    }

    private fun showSubmitDialog(success: Boolean, message: String?) {
        submitDialogBuilder.setView(
            layoutInflater.inflate(
                R.layout.authentication_submit_dialog,
                null
            )
        )
        submitDialogBuilder(success, message)
    }

    private fun submitDialogBuilder(success: Boolean, message: String?) {
        val dialog = submitDialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val animationView =
            dialog.findViewById<LottieAnimationView>(R.id.lottieAnimationView) as LottieAnimationView
        val title = dialog.findViewById<TextView>(R.id.textTitle) as TextView
        val subtitle = dialog.findViewById<TextView>(R.id.textSubtitle) as TextView
        val closeBtn = dialog.findViewById<ImageButton>(R.id.close_btn) as ImageButton
        val okayBtn = dialog.findViewById<Button>(R.id.okay_btn) as Button

        if (success) {
            val intent = Intent(activity, DashboardActivity::class.java)
            animationView.setAnimation(R.raw.success)
            title.text = requireContext().getString(R.string.auth_success_info, "Login")

            subtitle.visibility = View.GONE
            closeBtn.visibility = View.GONE
            okayBtn.visibility = View.GONE

            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
                startActivity(intent)
                requireActivity().finish()
            }, 1500)
        } else {
            animationView.setAnimation(R.raw.incorrect)
            title.text = requireContext().getString(R.string.auth_failed_title)

            if (message != null) {
                subtitle.text = requireContext().getString(R.string.auth_failed_info, "login", message)
            } else {
                subtitle.text = requireContext().getString(
                    R.string.auth_failed_info,
                    "login",
                    "Check your email or password again!"
                )
            }

            okayBtn.background =
                requireContext().getDrawable(R.drawable.rounded_danger_corner_button)

            closeBtn.setOnClickListener {
                dialog.dismiss()
            }

            okayBtn.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun setupView() {
        buildLoadingDialog()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        submitDialogBuilder = AlertDialog.Builder(requireContext(), R.style.WrapContentDialog)
    }

    private fun buildLoadingDialog() {
        loadingDialogBuilder = LoadingDialog(requireActivity())
        loadingDialog = loadingDialogBuilder.buildLoadingDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
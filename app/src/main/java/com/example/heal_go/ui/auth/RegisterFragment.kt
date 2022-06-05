package com.example.heal_go.ui.auth

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentRegisterBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.auth.viewmodel.AuthViewModel
import com.example.heal_go.util.LoadingDialog
import com.example.heal_go.util.Status
import com.wajahatkarim3.easyvalidation.core.Validator

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    /* build loading dialog */
    private lateinit var loadingDialogBuilder: LoadingDialog
    private lateinit var loadingDialog: AlertDialog

    private val navController: NavController by lazy {
        findNavController()
    }

    lateinit var submitDialogBuilder: AlertDialog.Builder

    private val authViewModel by viewModels<AuthViewModel> { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        submitDialogBuilder = AlertDialog.Builder(requireContext(), R.style.WrapContentDialog)

        setupView()

        return binding.root
    }

    private fun setupView() {
        buildLoadingDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            registerBtn.setOnClickListener {
                if (validation()) {
                    authViewModel.register(
                        etFullname.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )
                } else {
                    Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show()
                }
            }

            authViewModel.register.observe(viewLifecycleOwner) { result ->
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

            loginBtn.setOnClickListener {
                navController.navigate(R.id.registerFragment_to_loginFragment)
            }
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
            animationView.setAnimation(R.raw.success)
            title.text = requireContext().getString(R.string.auth_success_info, "Register")

            subtitle.visibility = View.GONE
            closeBtn.visibility = View.GONE
            okayBtn.visibility = View.GONE

            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
                navController.navigate(R.id.registerFragment_to_loginFragment)
            }, 1500)
        } else {
            animationView.setAnimation(R.raw.incorrect)
            title.text = requireContext().getString(R.string.auth_failed_title)
            if (message != null) {
                subtitle.text =
                    requireContext().getString(R.string.auth_failed_info, "register", message)
            } else {
                subtitle.text = requireContext().getString(
                    R.string.auth_failed_info,
                    "register",
                    "This email already registered to this application!"
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

    private fun validation(): Boolean {

        val etEmail = binding.etEmail.text
        val etFullname = binding.etFullname.text
        val etPassword = binding.etPassword.text

        val validatiorEmail = Validator(etEmail.toString()).apply {
            nonEmpty()
            validEmail()
            addErrorCallback {
                binding.etEmail.error = "Email is not valid!"
            }
            addSuccessCallback {
                binding.etEmail.error = null
            }
        }.check()


        val validatiorFullname = Validator(etFullname.toString()).apply {
            nonEmpty()
            noNumbers()
            addErrorCallback {
                binding.etFullname.error = "Name is not valid!"
            }
            addSuccessCallback {
                binding.etFullname.error = null
            }
        }.check()


        val validatorpassword = Validator(etPassword.toString()).apply {
            nonEmpty()
            minLength(8)
            atleastOneNumber()
            addErrorCallback {
                binding.etPassword.error = "Password is not valid!"
            }
            addSuccessCallback {
                binding.etPassword.error = null
            }
        }.check()

        return validatiorEmail && validatiorFullname && validatorpassword

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
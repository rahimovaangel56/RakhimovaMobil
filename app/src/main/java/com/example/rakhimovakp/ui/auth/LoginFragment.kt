package com.example.rakhimovakp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rakhimovakp.R
import com.example.rakhimovakp.auth.AuthManager
import com.example.rakhimovakp.auth.LoginResult
import com.example.rakhimovakp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())

        setupClickListeners()
        checkIfAlreadyLoggedIn()
        setupInputListeners()
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.registerTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupInputListeners() {
        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideError()
        }

        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideError()
        }
    }

    private fun checkIfAlreadyLoggedIn() {
        if (authManager.isLoggedIn()) {
            navigateToMainScreen()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            showError("Укажите email")
            return false
        }

        if (password.isEmpty()) {
            showError("Введите пароль")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Введите корректный email")
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        hideError()
        binding.progressBar.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
        binding.registerTextView.isEnabled = false

        authManager.login(email, password) { result ->
            binding.progressBar.visibility = View.GONE
            binding.loginButton.isEnabled = true
            binding.registerTextView.isEnabled = true

            when (result) {
                is LoginResult.Success -> {
                    showSuccess("Вход выполнен!")
                    navigateToMainScreen()
                }

                is LoginResult.Error -> {
                    showError(result.message)
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_catalogFragment)
    }

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.errorTextView.visibility = View.GONE
    }

    private fun showSuccess(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.rakhimovakp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rakhimovakp.R
import com.example.rakhimovakp.auth.AuthManager
import com.example.rakhimovakp.auth.LoginResult
import com.example.rakhimovakp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupInputListeners()
    }

    private fun setupClickListeners() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (validateInput(name, email, phone, password, confirmPassword)) {
                registerUser(name, email, phone, password)
            }
        }

        binding.loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun setupInputListeners() {
        binding.nameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideError()
        }
        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideError()
        }
        binding.phoneEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideError()
        }
        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideError()
        }
        binding.confirmPasswordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideError()
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty()) {
            showError("Укажите имя")
            return false
        }

        if (email.isEmpty()) {
            showError("Укажите email")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Введите корректный email")
            return false
        }

        if (phone.isEmpty()) {
            showError("Укажите номер телефона")
            return false
        }

        if (password.isEmpty()) {
            showError("Введите пароль")
            return false
        }

        if (password.length < 6) {
            showError("Пароль должен быть длиннее 6 символов")
            return false
        }

        if (password != confirmPassword) {
            showError("Пароли не совпадают")
            return false
        }

        return true
    }

    private fun registerUser(name: String, email: String, phone: String, password: String) {
        hideError()
        binding.progressBar.visibility = View.VISIBLE
        binding.registerButton.isEnabled = false
        binding.loginTextView.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            authManager.register(email, password, phone, name) { result ->

                activity?.runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.registerButton.isEnabled = true
                    binding.loginTextView.isEnabled = true

                    when (result) {
                        is LoginResult.Success -> {
                            showSuccess("Регистрация успешна!")
                            findNavController().navigate(R.id.action_registerFragment_to_catalogFragment)
                        }

                        is LoginResult.Error -> {
                            showError(result.message)
                        }
                    }
                }
            }
        }
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

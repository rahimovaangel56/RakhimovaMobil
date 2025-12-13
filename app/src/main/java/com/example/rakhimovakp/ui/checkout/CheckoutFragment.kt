package com.example.rakhimovakp.ui.checkout

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rakhimovakp.R
import com.example.rakhimovakp.data.models.Car
import com.example.rakhimovakp.databinding.FragmentCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModels()
    private val args: CheckoutFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCarInfo()
        setupFormListeners()
        setupObservers()
        setupSubmitButton()
    }

    private fun setupCarInfo() {
        val car = args.car
        viewModel.updateCar(car)

        with(binding) {
            tvCarBrandModel.text = "${car.brand} ${car.name}"
            tvCarPrice.text = formatPrice(car.price)
            tvCarDescription.text = car.description ?: ""
            tvBasePrice.text = formatPrice(car.price)
            tvTotalPrice.text = formatPrice(car.price)
        }
    }

    private fun setupFormListeners() {
        with(binding) {
            etFullName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.updateFullName(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            etPhone.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.updatePhone(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            etEmail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.updateEmail(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            etPassport.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.updatePassport(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbCash -> viewModel.updatePaymentMethod(PaymentMethod.CASH)
                    R.id.rbCredit -> viewModel.updatePaymentMethod(PaymentMethod.CREDIT)
                    R.id.rbCard -> viewModel.updatePaymentMethod(PaymentMethod.CARD)
                }
            }

            cbInsurance.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateInsurance(isChecked)
            }

            cbService.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateService(isChecked)
            }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            with(binding) {
                tvBasePrice.text = formatPrice(state.basePrice)
                tvOptionsPrice.text = formatPrice(state.optionsPrice)
                tvTotalPrice.text = formatPrice(state.totalPrice)
            }
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmitOrder.setOnClickListener {
            val state = viewModel.uiState.value

            // Валидация формы
            if (state.fullName.isBlank()) {
                showError("Введите ФИО")
                return@setOnClickListener
            }

            if (state.phone.isBlank()) {
                showError("Введите телефон")
                return@setOnClickListener
            }

            if (state.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
                showError("Введите корректный email")
                return@setOnClickListener
            }

            if (state.passport.isBlank() || state.passport.length != 10) {
                showError("Введите серию и номер паспорта (10 цифр)")
                return@setOnClickListener
            }

            viewModel.submitOrder {
                Toast.makeText(
                    requireContext(),
                    "Заказ успешно оформлен! С вами свяжется менеджер.",
                    Toast.LENGTH_LONG
                ).show()

                // Возвращаемся на предыдущий экран
                findNavController().popBackStack()
            }
        }
    }

    private fun formatPrice(price: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale("ru", "RU"))
        return "${formatter.format(price)} ₽"
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.rakhimovakp.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakhimovakp.data.models.Car
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun updateCar(car: Car) {
        _uiState.value = _uiState.value.copy(
            car = car,
            basePrice = car.price
        )
        calculateTotal()
    }

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName)
    }

    fun updatePhone(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone)
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassport(passport: String) {
        _uiState.value = _uiState.value.copy(passport = passport)
    }

    fun updatePaymentMethod(method: PaymentMethod) {
        _uiState.value = _uiState.value.copy(paymentMethod = method)
    }

    fun updateInsurance(hasInsurance: Boolean) {
        _uiState.value = _uiState.value.copy(hasInsurance = hasInsurance)
        calculateTotal()
    }

    fun updateService(hasService: Boolean) {
        _uiState.value = _uiState.value.copy(hasService = hasService)
        calculateTotal()
    }

    private fun calculateTotal() {
        val state = _uiState.value
        var total = state.basePrice

        if (state.hasInsurance) {
            total += INSURANCE_COST
        }

        if (state.hasService) {
            total += SERVICE_COST
        }

        _uiState.value = state.copy(
            optionsPrice = if (state.hasInsurance) INSURANCE_COST else 0.0 +
                    if (state.hasService) SERVICE_COST else 0.0,
            totalPrice = total
        )
    }

    fun submitOrder(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // TODO: Здесь будет логика сохранения заказа в базу данных
            // Например: отправка в репозиторий, API и т.д.

            // Временно просто показываем успех
            _uiState.value = _uiState.value.copy(isOrderSubmitted = true)
            onSuccess()
        }
    }

    companion object {
        const val INSURANCE_COST = 120000.0
        const val SERVICE_COST = 80000.0
    }
}

data class CheckoutUiState(
    val car: Car? = null,
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val passport: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val hasInsurance: Boolean = false,
    val hasService: Boolean = false,
    val basePrice: Double = 0.0,
    val optionsPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val isOrderSubmitted: Boolean = false
)

enum class PaymentMethod {
    CASH, CREDIT, CARD
}
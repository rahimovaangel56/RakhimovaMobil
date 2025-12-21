package com.example.rakhimovakp.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakhimovakp.auth.AuthManager
import com.example.rakhimovakp.auth.UserRole
import com.example.rakhimovakp.data.model.User
import com.example.rakhimovakp.data.models.Car
import com.example.rakhimovakp.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCarViewModel @Inject constructor(
    private val carRepository: CarRepository,
    private val authManager: AuthManager
) : ViewModel() {

    private val _addCarState = MutableStateFlow<AddCarState>(AddCarState.Idle)
    val addCarState: StateFlow<AddCarState> = _addCarState.asStateFlow()

    var user: User? = null

    init {
        user = authManager.currentUser.value
    }

    fun addCar(brand: String, name: String, price: Double, description: String?, imageUrl: String?) {
        viewModelScope.launch {
            _addCarState.value = AddCarState.Loading
            try {
                // Создаем новый объект Car. ID лучше генерировать более надежно, но для примера:
                val newCar = Car(
                    id = System.currentTimeMillis(), // ВРЕМЕННО! Для продакшена нужна логика генерации ID.
                    brand = brand,
                    name = name,
                    price = price,
                    description = description,
                    imageUrl = imageUrl // Сохраняем как строку URI
                )

                carRepository.insertCar(newCar)
                _addCarState.value = AddCarState.Success(newCar)
            } catch (e: Exception) {
                _addCarState.value = AddCarState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}

// Состояния UI для процесса добавления
sealed class AddCarState {
    object Idle : AddCarState()
    object Loading : AddCarState()
    data class Success(val car: Car) : AddCarState()
    data class Error(val message: String) : AddCarState()
}
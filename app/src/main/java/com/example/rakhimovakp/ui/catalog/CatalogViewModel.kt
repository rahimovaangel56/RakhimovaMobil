package com.example.rakhimovakp.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakhimovakp.data.models.Car
import com.example.rakhimovakp.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _cars = MutableLiveData<List<Car>>()
    val cars: LiveData<List<Car>> = _cars

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadCars()
    }

    fun loadCars() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                carRepository.initializeData()
                val carsList = carRepository.getAllCarsSync()
                _cars.value = carsList
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterCarsByBrand(brand: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filteredCars = if (brand == null) {
                    carRepository.getAllCarsSync()
                } else {
                    carRepository.getCarsByBrandSync(brand)
                }
                _cars.value = filteredCars
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка фильтрации: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

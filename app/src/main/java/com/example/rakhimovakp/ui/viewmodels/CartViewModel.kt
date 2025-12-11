package com.example.rakhimovakp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakhimovakp.data.model.CartItem
import com.example.rakhimovakp.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val total = items.map { list -> list.sumOf { it.carPrice * it.quantity } }
    val count = cartRepository.observeCount()

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.observeCart().collect { list ->
                _items.value = list
            }
        }
    }

    fun add(carId: Long, carName: String, carPrice: Double, imageUrl: String? = null) {
        viewModelScope.launch {
            cartRepository.addOrIncrement(carId, carName, carPrice, imageUrl)
        }
    }

    fun decrement(carId: Long) {
        viewModelScope.launch {
            cartRepository.decrementOrRemove(carId)
        }
    }

    fun remove(carId: Long) {
        viewModelScope.launch {
            cartRepository.remove(carId)
        }
    }

    fun clear() {
        viewModelScope.launch {
            cartRepository.clear()
            _items.value = emptyList()
        }
    }
}

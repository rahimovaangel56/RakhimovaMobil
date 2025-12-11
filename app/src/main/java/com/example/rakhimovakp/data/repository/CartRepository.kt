package com.example.rakhimovakp.data.repository

import com.example.rakhimovakp.data.local.dao.CartDao
import com.example.rakhimovakp.data.model.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun observeCart(): Flow<List<CartItem>> = cartDao.getAllCartItems()

    fun observeCount(): Flow<Int> = cartDao.getCartItemsCount()

    suspend fun addOrIncrement(carId: Long, carName: String, carPrice: Double, imageUrl: String? = null) {
        val existing = cartDao.getCartItemByCarId(carId)
        val updated = if (existing != null) {
            existing.copy(quantity = existing.quantity + 1)
        } else {
            CartItem(
                carId = carId,
                carName = carName,
                carPrice = carPrice,
                quantity = 1,
                imageUrl = imageUrl
            )
        }
        cartDao.insertCartItem(updated)
    }

    suspend fun decrementOrRemove(carId: Long) {
        val existing = cartDao.getCartItemByCarId(carId) ?: return
        if (existing.quantity > 1) {
            cartDao.insertCartItem(existing.copy(quantity = existing.quantity - 1))
        } else {
            cartDao.deleteCartItemByCarId(carId)
        }
    }

    suspend fun remove(carId: Long) {
        cartDao.deleteCartItemByCarId(carId)
    }

    suspend fun clear() = cartDao.clearCart()
}

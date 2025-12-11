package com.example.rakhimovakp.data.local.dao

import androidx.room.*
import com.example.rakhimovakp.data.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE carId = :carId")
    suspend fun getCartItemByCarId(carId: Long): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE carId = :carId")
    suspend fun deleteCartItemByCarId(carId: Long)

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemsCount(): Flow<Int>

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}


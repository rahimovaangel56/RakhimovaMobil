package com.example.rakhimovakp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val carId: Long,
    val carName: String,
    val carPrice: Double,
    val quantity: Int = 1,
    val imageUrl: String? = null
) : Parcelable
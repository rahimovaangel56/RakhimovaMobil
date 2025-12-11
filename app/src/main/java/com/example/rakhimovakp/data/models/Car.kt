package com.example.rakhimovakp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey
    val id: Long,
    val brand: String,
    val name: String,
    val price: Double,
    val description: String? = null,
    val imageUrl: String? = null
)
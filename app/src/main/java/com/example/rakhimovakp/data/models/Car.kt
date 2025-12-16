package com.example.rakhimovakp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) // <-- Ключевое изменение
    val id: Long = 0, // При создании объекта передавайте 0
    val brand: String,
    val name: String,
    val price: Double,
    val description: String? = null,
    val imageUrl: String? = null
) : Parcelable
package com.example.rakhimovakp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rakhimovakp.auth.UserRole

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val email: String,
    val password: String,
    val phone: String,
    val name: String = "",
    val role: UserRole,
    val dealershipId: String? = null,
    val createdAt: Long,
)

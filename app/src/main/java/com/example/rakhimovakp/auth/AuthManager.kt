package com.example.rakhimovakp.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import androidx.core.content.edit

class AuthManager(private val context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("rakhimova_auth", Context.MODE_PRIVATE)
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val mockUsers = listOf(
        User(
            id = "1",
            email = "admin@lapdigital.com",
            phone = "+79999999999",
            name = "Администратор Лапдиджитал",
            role = UserRole.ADMIN,
            createdAt = System.currentTimeMillis()
        ),
        User(
            id = "2",
            email = "manager@haval.com",
            phone = "+78888888888",
            name = "Менеджер Haval",
            role = UserRole.DEALER_MANAGER,
            dealershipId = "haval_dealership",
            createdAt = System.currentTimeMillis()
        ),
        User(
            id = "3",
            email = "manager@changan.com",
            phone = "+77777777777",
            name = "Менеджер Changan",
            role = UserRole.DEALER_MANAGER,
            dealershipId = "changan_dealership",
            createdAt = System.currentTimeMillis()
        ),
        User(
            id = "4",
            email = "customer@example.com",
            phone = "+76666666666",
            name = "Гость",
            role = UserRole.CUSTOMER,
            createdAt = System.currentTimeMillis()
        )
    )

    fun login(email: String, password: String, onResult: (LoginResult) -> Unit) {
        val user = mockUsers.find { it.email == email }
        loadUserFromPrefs()

        if ((user != null || _currentUser.value != null) && isValidPassword(password)) {
            saveUserToPrefs((user ?: _currentUser.value)!!)
            _currentUser.value = user
            onResult(LoginResult.Success)
        } else {
            onResult(LoginResult.Error("Неверный email или пароль"))
        }
    }

    fun register(
        email: String,
        password: String,
        phone: String,
        name: String,
        onResult: (LoginResult) -> Unit,
    ) {
        if (mockUsers.any { it.email == email }) {
            onResult(LoginResult.Error("Пользователь с таким email уже существует"))
            return
        }

        val newUser = User(
            id = (mockUsers.size + 1).toString(),
            email = email,
            phone = phone,
            name = name,
            role = UserRole.CUSTOMER,
            createdAt = System.currentTimeMillis()
        )

        saveUserToPrefs(newUser)
        _currentUser.value = newUser
        onResult(LoginResult.Success)
    }

    fun logout() {
        sharedPreferences.edit { clear() }
        _currentUser.value = null
    }

    fun isLoggedIn(): Boolean {
        return _currentUser.value != null
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun saveUserToPrefs(user: User) {
        val userJson = Gson().toJson(user)
        sharedPreferences.edit { putString("current_user", userJson) }
    }

    private fun loadUserFromPrefs() {
        val userJson = sharedPreferences.getString("current_user", null)
        userJson?.let {
            val user = Gson().fromJson(it, User::class.java)
            _currentUser.value = user
        }
    }

    init {
        loadUserFromPrefs()
    }
}

data class User(
    val id: String,
    val email: String,
    val phone: String,
    val name: String = "",
    val role: UserRole,
    val dealershipId: String? = null,
    val createdAt: Long,
)

enum class UserRole {
    ADMIN, DEALER_MANAGER, CUSTOMER, UNAUTHORIZED
}

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

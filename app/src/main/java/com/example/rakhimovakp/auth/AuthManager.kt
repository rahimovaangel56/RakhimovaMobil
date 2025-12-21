package com.example.rakhimovakp.auth

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rakhimovakp.data.local.dao.UserDao
import com.example.rakhimovakp.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AuthManager(
    private val context: Context,
    private val userDao: UserDao,
) {

    private val isDefaultUserCreated =
        context.getSharedPreferences("rakhimova_default_users", Context.MODE_PRIVATE)

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    init {
        val shouldCreateUsers = isDefaultUserCreated.getBoolean("key", false)
        if (!shouldCreateUsers) {
            val defaultUsers = listOf(
                User(
                    email = "admin@lapdigital.com",
                    phone = "+79999999999",
                    password = hashPassword("ADMIN_PASSWORD"),
                    name = "Администратор Лапдиджитал",
                    role = UserRole.ADMIN,
                    createdAt = System.currentTimeMillis()
                ),
                User(
                    email = "manager@haval.com",
                    phone = "+78888888888",
                    name = "Менеджер Haval",
                    password = hashPassword("DEALER_MANAGER_HAVAL"),
                    role = UserRole.DEALER_MANAGER,
                    dealershipId = "haval_dealership",
                    createdAt = System.currentTimeMillis()
                ),
                User(
                    email = "manager@changan.com",
                    phone = "+77777777777",
                    name = "Менеджер Changan",
                    password = hashPassword("DEALER_MANAGER_CHANGAN"),
                    role = UserRole.DEALER_MANAGER,
                    dealershipId = "changan_dealership",
                    createdAt = System.currentTimeMillis()
                ),
                User(
                    email = "customer@example.com",
                    phone = "+76666666666",
                    name = "Гость",
                    password = hashPassword("CUSTOMER"),
                    role = UserRole.CUSTOMER,
                    createdAt = System.currentTimeMillis()
                )
            )

            CoroutineScope(Dispatchers.IO).launch {
                userDao.registerUsers(defaultUsers)
                isDefaultUserCreated.edit { putBoolean("key", true) }
            }
        }
    }

    suspend fun login(email: String, password: String, onResult: (LoginResult) -> Unit) {
        val user = userDao.getUsers().find {
            it.email == email &&
                    it.password == hashPassword(password)
        }

        if (user != null && isValidPassword(password)) {
            _currentUser.postValue(user)
            return onResult(LoginResult.Success)
        }

        return onResult(LoginResult.Error("Неверный email или пароль"))
    }

    suspend fun register(
        email: String,
        password: String,
        phone: String,
        name: String,
        onResult: (LoginResult) -> Unit,
    ) {
        if (userDao.getUsers().any { it.email == email }) {
            onResult(LoginResult.Error("Пользователь с таким email уже существует"))
            return
        }

        val newUser = User(
            email = email,
            password = hashPassword(password),
            phone = phone,
            name = name,
            role = UserRole.CUSTOMER,
            createdAt = System.currentTimeMillis()
        )
        userDao.registerUser(newUser)
        _currentUser.postValue(newUser)
        onResult(LoginResult.Success)
    }

    fun logout() {
        _currentUser.value = null
    }

    fun isLoggedIn(): Boolean {
        return _currentUser.value != null
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { acc, string -> acc + "%02x".format(string) }
    }
}

enum class UserRole {
    ADMIN, DEALER_MANAGER, CUSTOMER, UNAUTHORIZED
}

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

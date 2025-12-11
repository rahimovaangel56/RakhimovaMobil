package com.example.rakhimovakp.ui.login

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)

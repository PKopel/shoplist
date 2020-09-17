package com.example.shoplist.ui.login

import io.realm.mongodb.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: User? = null,
    val error: String? = null
)
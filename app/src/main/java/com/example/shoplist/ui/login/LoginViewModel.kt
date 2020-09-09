package com.example.shoplist.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.R
import com.example.shoplist.TAG
import com.example.shoplist.shopListApp
import io.realm.mongodb.Credentials

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        val credentials = Credentials.emailPassword(username, password)
        shopListApp.emailPasswordAuth.registerUserAsync(username, password) {
            if (!it.isSuccess) {
                _loginResult.value = LoginResult(error = R.string.login_failed)
                Log.e(TAG(), "Error: ${it.error}")
            }
        }
        shopListApp.loginAsync(credentials) {
            if (!it.isSuccess) {
                _loginResult.value = LoginResult(error = R.string.login_failed)
                Log.e(TAG(), "Error: ${it.error}")
            } else {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = it.get().name ?: ""))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else false
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
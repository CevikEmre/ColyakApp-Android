package com.example.colyak.session

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.colyak.model.TokenData
import com.example.colyak.service.RefreshTokenService
import com.example.colyak.viewmodel.loginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionManager(context: Context) {
    companion object {
        private const val PREF_NAME = "session_pref"
        private const val KEY_TOKEN = "key_token"
        private const val KEY_REFRESH_TOKEN = "key_refresh_token"
        private const val EMAIL = "email"
        private const val KEY_USERNAME = "key_username"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(KEY_TOKEN, null)
    }

    suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    suspend fun getUserName(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(KEY_USERNAME, null)
    }

    suspend fun getEmail(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(EMAIL, null)
    }

    suspend fun saveEmail(email: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit {
            putString(EMAIL, email)
            Log.e("EMAIL", email)
        }
    }

    suspend fun refreshToken(refreshToken: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val tokenResponse = RefreshTokenService.refreshToken(TokenData(refreshToken))
            tokenResponse?.let {
                saveToken(it.token, it.refreshToken, it.userName)
                it.token.let { token -> loginResponse.token = token }
                it.refreshToken.let { refreshToken -> loginResponse.refreshToken = refreshToken }
                it.userName.let { userName -> loginResponse.userName = userName }
                Log.e("USER", tokenResponse.toString())
                true
            } ?: run {
                clearSession()
                false
            }
        } catch (e: Exception) {
            Log.e("SessionManager", "Token refresh failed", e)
            clearSession()
            false
        }
    }
    suspend fun saveToken(token: String?, refreshToken: String?, userName: String?) {
        sharedPreferences.edit {
            putString(KEY_TOKEN, token)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_USERNAME, userName)
        }
        Log.e(
            "SessionManager",
            "Token saved: $token, Refresh Token saved: $refreshToken, Username saved: $userName"
        )
    }

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
        loginResponse.token = ""
        loginResponse.refreshToken = ""
        loginResponse.userName = ""
        Log.e("SessionManager", "Session cleared.")
    }
}

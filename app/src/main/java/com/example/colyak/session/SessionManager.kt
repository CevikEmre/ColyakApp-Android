package com.example.colyak.session

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.colyak.model.TokenData
import com.example.colyak.service.RefreshTokenService
import com.example.colyak.viewmodel.loginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionManager(context: Context) {
    companion object {
        private const val PREF_NAME = "session_pref"
        private const val KEY_TOKEN = "key_token"
        private const val KEY_REFRESH_TOKEN = "key_refresh_token"
        private const val EMAIL = "email"
        private const val KEY_TOKEN_TIMESTAMP = "key_token_timestamp"
        private const val KEY_USERNAME = "key_username"
        private const val KEY_REFRESH_TOKEN_TIMESTAMP = "key_refresh_token_timestamp"
        private const val TOKEN_VALIDITY_DURATION = 30 * 1000L
        private const val REFRESH_TOKEN_VALIDITY_DURATION = 60 * 1000L
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    init {
        startTokenRefresh(context)
    }

    private fun startTokenRefresh(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val token = getToken()
                val refreshToken = getRefreshToken()

                // Access token check
                if (token != null) {
                    val tokenTimestamp = sharedPreferences.getLong(KEY_TOKEN_TIMESTAMP, 0)
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - tokenTimestamp >= TOKEN_VALIDITY_DURATION) {
                        clearSession()
                        //DialogHelper.showSessionExpiredDialog(context)
                        Log.e("SessionManager", "Access token expired, session cleared.")
                    }
                }

                // Refresh token check and refresh
                if (refreshToken != null) {
                    val refreshTokenTimestamp = sharedPreferences.getLong(KEY_REFRESH_TOKEN_TIMESTAMP, 0)
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - refreshTokenTimestamp >= REFRESH_TOKEN_VALIDITY_DURATION) {
                        clearSession()
                        //DialogHelper.showSessionExpiredDialog(context)
                        Log.e("SessionManager", "Refresh token expired, session cleared.")
                    } else {
                        // Trigger refresh token request
                        refreshToken(refreshToken)
                    }
                } else {
                    clearSession()
                    //DialogHelper.showSessionExpiredDialog(context)
                    Log.e("SessionManager", "Refresh token is null, session cleared.")
                }

                // Wait for 30 seconds
                delay(30 * 1000L)
            }
        }
    }

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

    private suspend fun refreshToken(refreshToken: String) {
        val tokenResponse = RefreshTokenService.refreshToken(TokenData(refreshToken))
        tokenResponse?.let {
            saveToken(it.token, it.refreshToken, it.userName)
            it.token?.let { token -> loginResponse.token = token }
            it.refreshToken?.let { refreshToken -> loginResponse.refreshToken = refreshToken }
            it.userName?.let { userName -> loginResponse.userName = userName }
            Log.e("USER", tokenResponse.toString())
        } ?: run {
            clearSession()
            Log.e("CLEARUSER", "Failed to refresh token, session cleared.")
        }
    }

    suspend fun saveToken(token: String, refreshToken: String, userName: String) {
        sharedPreferences.edit {
            putString(KEY_TOKEN, token)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_USERNAME, userName)
            putLong(KEY_TOKEN_TIMESTAMP, System.currentTimeMillis())
            putLong(KEY_REFRESH_TOKEN_TIMESTAMP, System.currentTimeMillis())
        }
        Log.e("SessionManager", "Token saved: $token, Refresh Token saved: $refreshToken, Username saved: $userName")
    }

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
        loginResponse.token = ""
        loginResponse.refreshToken = ""
        loginResponse.userName = ""
    }
}

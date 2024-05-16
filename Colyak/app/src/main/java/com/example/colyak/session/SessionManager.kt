import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.colyak.service.RefreshTokenService
import com.example.colyak.viewmodel.loginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionManager(private val context: Context) {
    companion object {
        private const val PREF_NAME = "session_pref"
        private const val KEY_TOKEN = ""
        private const val KEY_REFRESH_TOKEN = ""
        private const val KEY_TOKEN_TIMESTAMP = ""
        private const val KEY_USERNAME = ""
        private const val TOKEN_VALIDITY_DURATION = 30 * 1000L
        private const val REFRESH_TOKEN_VALIDITY_DURATION = 60 * 1000L
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()
        val tokenTimestamp = sharedPreferences.getLong(KEY_TOKEN_TIMESTAMP, 0)
        val tokenExpirationTime = tokenTimestamp + TOKEN_VALIDITY_DURATION
        if (currentTime < tokenExpirationTime) {
            sharedPreferences.getString(KEY_TOKEN, null)
        } else {
            refreshToken()
            null
        }
    }

    suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    suspend fun getUserName(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(KEY_USERNAME, null)
    }

    private suspend fun refreshToken() {
        var retryCount = 0
        while (retryCount < 2) {
            val token = getToken()
            token?.let { tokenValue ->
                val loginResponse = RefreshTokenService.refreshToken(tokenValue)
                loginResponse?.let {
                    saveToken(it.token, it.refreshToken, it.userName)
                    Log.e("USER",loginResponse.toString())
                    return
                }
            }
            retryCount++
            if (retryCount == 2) {
                clearSession()
                Log.e("CLEARUSER",loginResponse.toString())
                return
            }
        }
    }


    private fun saveToken(token: String, refreshToken: String, userName: String) {
        sharedPreferences.edit {
            putString(KEY_TOKEN, token)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_USERNAME, userName)
            putLong(KEY_TOKEN_TIMESTAMP, System.currentTimeMillis())
        }
    }
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
        loginResponse.token = ""
        loginResponse.refreshToken = ""
        loginResponse.userName = ""
    }
}

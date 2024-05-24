package com.example.colyak.session

import android.content.Context
import android.util.Log
import com.example.colyak.model.TokenData
import com.example.colyak.service.RefreshTokenService.Companion.refreshToken

object TokenCheckHelper {
    suspend fun checkTokenAndShowDialog(context: Context) {
        val sessionManager = SessionManager(context)
        val token = sessionManager.getToken()
        if (token.isNullOrEmpty()) {
            DialogHelper.showSessionExpiredDialog(context)
        } else {
            val refreshToken = sessionManager.getRefreshToken()
            if (refreshToken == null) {

                Log.e("TokenCheckHelper", "Refresh token is null, session not cleared.")
            } else {
                refreshToken(tokenData = TokenData(refreshToken))
            }
        }
    }
}

package com.example.colyak.retrofit

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.colyak.screens.ColyakApp
import com.example.colyak.screens.NavControllerHolder
import com.example.colyak.screens.Screens
import com.example.colyak.session.SessionManager
import com.example.colyak.viewmodel.loginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    val sessionManager = SessionManager(ColyakApp.applicationContext())
    val scope = CoroutineScope(Dispatchers.IO)
    private var alertDialogShown = false
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val modifiedRequest = if (url.contains("/api/users/")) {
            request.newBuilder().build()
        } else {
            request.newBuilder()
                .addHeader("Authorization", "Bearer ${loginResponse.token}")
                .build()
        }

        val response = chain.proceed(modifiedRequest)
        if (response.code == 401 || response.code == 601) {
            scope.launch {
                sessionManager.refreshToken(loginResponse.refreshToken)
            }
        }
        if (response.code == 602) {
            Handler(Looper.getMainLooper()).post {
                showRefreshTokenExpiredAlert(ColyakApp.applicationContext())
                alertDialogShown = true
            }
        }
        return response
    }

    private fun showRefreshTokenExpiredAlert(context: Context) {
        val currentActivity = getCurrentActivity(context)
        currentActivity?.let {
            AlertDialog.Builder(it)
                .setTitle("Oturum süresi doldu")
                .setMessage("Lütfen uygulamadan çıkış yaparak tekrar giriniz")
                .setPositiveButton("Tamam") { dialog, _ ->
                    dialog.dismiss()
                    navigateToLogin()
                    alertDialogShown = false
                }
                .create()
                .show()
        }
    }

    private fun navigateToLogin() {
        NavControllerHolder.navController?.navigate(Screens.Login.screen) {
            popUpTo(Screens.Login.screen) { inclusive = true }
        }
    }

    private fun getCurrentActivity(context: Context): Activity? {
        return (context as? ColyakApp)?.currentActivity
    }
}

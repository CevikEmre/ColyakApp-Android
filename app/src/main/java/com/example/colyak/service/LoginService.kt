package com.example.colyak.service

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.example.colyak.`interface`.LoginInterface
import com.example.colyak.model.LoginResponse
import com.example.colyak.model.data.LoginData
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.screens.Screens
import com.example.colyak.viewmodel.verificationId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginService {
    companion object {
        suspend fun login(context: Context, navController: NavController, email: String, password: String): LoginResponse? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(LoginInterface::class.java)
                        .login(LoginData(email, password))
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        val errorCode = response.code()
                        if (errorCode == 619) {
                            withContext(Dispatchers.Main) {
                                showUnverifyError(context, navController)
                                verificationId = response.errorBody()?.string().toString()
                                Log.e("VERIFICATION_ID samet", verificationId)
                            }
                        } else if (errorCode == 631) {
                            withContext(Dispatchers.Main) {
                                showEmailOrPasswordError(context)
                            }
                        }
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "login",
                            "login request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("LOGIN", "Error sending login request", e)
                    null
                }
            }
        }

        private fun showUnverifyError(context: Context, navController: NavController) {
            AlertDialog.Builder(context)
                .setTitle("Doğrulama")
                .setMessage("Hesabınız doğrulanamadı. Lütfen e-posta adresinizi kontrol ediniz ve doğrualama ekranından doğrulama kodunu giriniz.")
                .setPositiveButton("Tamam") { dialog, _ ->
                    navController.navigate(Screens.VerificationScreen.screen)
                    dialog.dismiss()
                }
                .show()
        }

        private fun showEmailOrPasswordError(context: Context) {
            AlertDialog.Builder(context)
                .setTitle("Giriş Başarısız")
                .setMessage("Geçersiz Kullanıcı Adı veya Şifre")
                .setPositiveButton("Tamam") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}

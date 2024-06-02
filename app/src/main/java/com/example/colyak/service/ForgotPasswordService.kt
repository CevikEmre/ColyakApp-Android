package com.example.colyak.service


import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.example.colyak.`interface`.ForgotPasswordInterface
import com.example.colyak.model.StringResponse
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.screens.Screens
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class ForgotPasswordService {

    suspend fun forgotPassword(email: String, context: Context,navController: NavController): Unit? {
        return withContext(Dispatchers.IO) {
            try {
                Log.e("ForgotPasswordService", "Request started")
                val response = RetrofitClient.getClient(baseUrl)
                    .create(ForgotPasswordInterface::class.java)
                    .forgotPassword(email)
                if (response.code() == 200){
                    withContext(Dispatchers.Main) {
                        showSuccessAlert(context,navController)
                    }
                }
                if (response.isSuccessful) {
                    Log.e("ForgotPasswordService", "Response successful")
                    response.body()
                } else {
                    Log.e("ForgotPasswordService", "Response failed with code: ${response.code()}")

                    val statusCode = response.code()
                    if (statusCode == 500) {
                        withContext(Dispatchers.Main) {
                            showEmailNotFoundError(context)
                        }
                    }
                    val errorJson = response.errorBody()?.string()
                    Log.e("ForgotPasswordService", "Error body: $errorJson")

                    if (errorJson != null && errorJson.startsWith("{")) {
                        val errorResponse = Gson().fromJson(errorJson, StringResponse::class.java)
                        val errorMessage = errorResponse?.message ?: "Unknown error"
                        Log.e("ForgotPasswordService", "Parsed error: $errorMessage")
                    } else {
                        Log.e("ForgotPasswordService", "Raw error: $errorJson")
                    }
                    null
                }
            } catch (e: Exception) {
                Log.e("ForgotPasswordService", "Exception occurred", e)
                null
            }
        }
    }
}

private fun showEmailNotFoundError(context: Context) {
    Log.e("ForgotPasswordService", "Showing email not found error")
    AlertDialog.Builder(context)
        .setTitle("Mail Bulunamadı")
        .setMessage("Lütfen girdiğiniz mail adresini kontrol ediniz.")
        .setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

private fun showSuccessAlert(context: Context,navController: NavController) {
    Log.e("ForgotPasswordService", "Showing success alert")
    AlertDialog.Builder(context)
        .setTitle("Email Gönderildi")
        .setMessage("Lütfen e-mail adresinize gelen linke tıklayarak şifrenizi yenileyiniz.")
        .setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
            navController.navigate(Screens.Login.screen)
        }
        .show()
}

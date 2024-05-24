package com.example.colyak.session

import android.app.AlertDialog
import android.content.Context

object DialogHelper {
    fun showSessionExpiredDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Session Expired")
            .setMessage("Your session has expired. Please login again.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()

            }
            .setCancelable(false)
            .show()
    }
}
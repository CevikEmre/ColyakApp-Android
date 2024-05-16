package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colyak.model.Receipt
import com.example.colyak.model.data.FavoriteData
import com.example.colyak.service.FavoriteService

class FavoriteViewModel : ViewModel() {
    private var _favoriteReceiptList = MutableLiveData<List<Receipt?>>(emptyList())
    val favoriteReceiptList: LiveData<List<Receipt?>> = _favoriteReceiptList


    suspend fun getAllFavoriteReceipts(): List<Receipt?>? {
        return try {
            val result = FavoriteService.getAllFavoriteReceipts()
            _favoriteReceiptList.value = result ?: emptyList()
            favoriteReceiptList.value
        } catch (e: Exception) {
            Log.e("FavoriteViewModel", "Fail", e)
            emptyList()
        }
    }

    suspend fun likeReceipt(favoriteData: FavoriteData): Boolean {
        return try {
            FavoriteService.likeReceipt(favoriteData)
            true
        } catch (e: Exception) {
            Log.e("CommentViewModel", "Fail", e)
            false
        }
    }

    suspend fun unlikeReceipt(favoriteData: FavoriteData) {
        try {
            FavoriteService.unlikeReceipt(favoriteData)

        } catch (e: Exception) {
            Log.e("CommentViewModel", "Fail", e)

        }
    }
}
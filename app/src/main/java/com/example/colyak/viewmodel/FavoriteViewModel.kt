package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colyak.model.Receipt
import com.example.colyak.model.data.FavoriteData
import com.example.colyak.service.FavoriteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoriteViewModel : ViewModel() {
    private var _favoriteReceiptList = MutableLiveData<List<Receipt?>>(emptyList())
    val favoriteReceiptList: LiveData<List<Receipt?>> = _favoriteReceiptList

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    suspend fun getAllFavoriteReceipts(): List<Receipt?>? {
        return try {
            _loading.value = true
            val result = FavoriteService.getAllFavoriteReceipts()
            _favoriteReceiptList.value = result ?: emptyList()
            favoriteReceiptList.value
        } catch (e: Exception) {
            Log.e("FavoriteViewModel", "Fail", e)
            emptyList()
        }
        finally {
            _loading.value = false
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

    suspend fun unlikeReceipt(favoriteData: FavoriteData?) {
        try {
            favoriteData?.let { FavoriteService.unlikeReceipt(it) }

        } catch (e: Exception) {
            Log.e("CommentViewModel", "Fail", e)

        }
    }
}
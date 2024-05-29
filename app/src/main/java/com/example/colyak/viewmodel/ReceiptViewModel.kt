package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.Receipt
import com.example.colyak.screens.globalReceiptList
import com.example.colyak.service.ReceiptService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReceiptViewModel : ViewModel() {
    private val _receiptList = MutableStateFlow<List<Receipt?>?>(emptyList())
    val receiptList: StateFlow<List<Receipt?>?> = _receiptList

    private val _favoriteReceiptList = MutableStateFlow<List<Receipt?>?>(emptyList())
    val favroiteReceiptList: StateFlow<List<Receipt?>?> = _favoriteReceiptList


    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _favoriteLoading = MutableStateFlow(false)
    val favoriteLoading: StateFlow<Boolean> = _favoriteLoading

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _filteredReceiptList = MutableStateFlow(globalReceiptList)
    val filteredReceiptList: StateFlow<List<Receipt?>?> =
        searchText.combine(_filteredReceiptList) { text, receipts ->
            if (text.isBlank()) {
                globalReceiptList
            } else {
                receipts?.filter { receipt ->
                    receipt?.receiptName?.contains(
                        text.trim(),
                        ignoreCase = true
                    )!!
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = globalReceiptList
        )

    suspend fun getAll() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = ReceiptService.getAll()
                _receiptList.value = result ?: emptyList()
                Log.e("ReceiptList12", receiptList.value.toString())

            } catch (e: Exception) {
                Log.e("ReceiptScreenVM", "Fail", e)
            } finally {
                _loading.value = false
            }

        }
    }
    suspend fun getFavorite5Receipts() {
        viewModelScope.launch {
            _favoriteLoading.value = true
            try {
                val result = ReceiptService.getFavorite5Receipts()
                _favoriteReceiptList.value= result ?: emptyList()
                Log.e("ReceiptList12", receiptList.value.toString())

            } catch (e: Exception) {
                Log.e("ReceiptScreenVM", "Fail", e)
            } finally {
                _favoriteLoading.value = false
            }

        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

}
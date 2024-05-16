package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.ReadyFoods
import com.example.colyak.screens.globalReadyFoodList
import com.example.colyak.service.ReadyFoodService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReadyFoodScreenViewModel : ViewModel() {
    private val _readyFoodList = MutableStateFlow<List<ReadyFoods?>?>(emptyList())
    var readyFoodList: StateFlow<List<ReadyFoods?>?> = _readyFoodList

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _filteredReadyFoodList = MutableStateFlow(globalReadyFoodList)
    val filteredReadyFoodList: StateFlow<List<ReadyFoods?>?> =
        searchText.combine(_filteredReadyFoodList) { text, readyFoods ->
            if (text.isBlank()) {
                globalReadyFoodList
            } else {
                readyFoods?.filter { readyFood ->
                    readyFood?.readyFoodName?.contains(
                        text.trim(),
                        ignoreCase = true
                    ) ?: true
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = globalReadyFoodList
        )

    fun getAllReadyFoods() {
        viewModelScope.launch {
            try {
                val result = ReadyFoodService.getAllReadyFoods()
                _readyFoodList.value = result ?: emptyList()
                Log.e("deneme", _readyFoodList.value.toString())
                Log.e("göster", readyFoodList.toString())

            }catch (e:Exception){
                Log.e("ReceiptScreenVM", "Fail", e)
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
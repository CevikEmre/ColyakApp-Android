package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.PDFResponse
import com.example.colyak.service.PDFService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PDFViewModel:ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _pdfList = MutableStateFlow<List<PDFResponse?>?>(emptyList())
    val pdfList: StateFlow<List<PDFResponse?>?> = _pdfList


    suspend fun getAllPdfs() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = PDFService.getPDFs()
                _pdfList.value = result ?: emptyList()
                Log.e("PDFLIST", pdfList.value.toString())
            } catch (e: Exception) {
                Log.e("PDFViewModel", "Fail", e)
            } finally {
                _loading.value = false
            }
        }
    }
}
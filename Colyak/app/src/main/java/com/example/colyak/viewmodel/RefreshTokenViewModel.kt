package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.LoginResponse
import com.example.colyak.service.RefreshTokenService
import kotlinx.coroutines.launch

class RefreshTokenViewModel:ViewModel() {

    private var _refreshToken = MutableLiveData<LoginResponse?>(LoginResponse("","",""))
    val refreshToken: MutableLiveData<LoginResponse?> = _refreshToken

    suspend fun refreshToken(token:String){
        viewModelScope.launch {
            try {
                val response = RefreshTokenService.refreshToken(token)
                if (response != null){
                    _refreshToken.value = response
                }
            }
            catch (e:Exception){
                Log.e("RefreshTokenViewModel", "Fail", e)
            }
        }
    }
}
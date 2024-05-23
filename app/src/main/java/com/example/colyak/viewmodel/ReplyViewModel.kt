package com.example.colyak.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.CommentRepliesResponse
import com.example.colyak.model.ReplyResponse
import com.example.colyak.model.data.ReplyData
import com.example.colyak.service.ReplyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

var globalReplyList = mutableStateListOf<ReplyResponse?>()

class ReplyViewModel : ViewModel() {
    private val _replyList = MutableLiveData<List<ReplyResponse?>?>(emptyList())
    var replyList: LiveData<List<ReplyResponse?>?> = _replyList

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _commentRepliesList = MutableLiveData<List<CommentRepliesResponse?>?>(emptyList())
    val commentRepliesList: MutableLiveData<List<CommentRepliesResponse?>?> = _commentRepliesList


    suspend fun getCommentsById(commentId: Long): List<ReplyResponse?>? {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = ReplyService().getRepliesById(commentId)
                _replyList.value = response ?: emptyList()
                globalReplyList = _replyList.value!!.toMutableStateList()
                Log.e("aa",globalReplyList.toString())
            } catch (e: Exception) {
                Log.e("ReplyViewModel", "Fail", e)
            }
            finally {
                _loading.value = false
            }
        }
        return _replyList.value
    }

    suspend fun createReply(replyData: ReplyData): Boolean {
        return try {
            ReplyService().createReply(replyData)
            true
        } catch (e: Exception) {
            Log.e("ReplyViewModel", "Fail", e)
            false
        }
    }
    suspend fun getCommentsRepliesByReceiptId(receiptId: Long): List<ReplyResponse?>? {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = ReplyService().getCommentsRepliesByReceiptId(receiptId)
                _commentRepliesList.value = response ?: emptyList()
                Log.e("aa",_commentRepliesList.toString())
            } catch (e: Exception) {
                Log.e("ReplyViewModel", "Fail", e)
            }
            finally {
                _loading.value = false
            }
        }
        return _replyList.value
    }
    suspend fun deleteReply(replyId: Long) {
        try {
            _loading.value = true
            ReplyService().deleteReply(replyId)
        } catch (e: Exception) {
            Log.e("ReplyViewModel", "Fail", e)

        } finally {
            _loading.value = false
        }
    }
}
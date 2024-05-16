package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.Comment
import com.example.colyak.model.data.CommentData
import com.example.colyak.service.CommentService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {
    private var _commentList = MutableStateFlow<List<Comment>>(emptyList())
    val commentList: MutableStateFlow<List<Comment>> = _commentList

    private var _comment = MutableStateFlow(CommentData(0, ""))
    val comment: StateFlow<CommentData> = _comment


    suspend fun getCommentsById(receiptId: Long) {
        viewModelScope.launch {
            try {
                val result = CommentService.getCommentsById(receiptId)
                _commentList.value = result ?: emptyList()
            } catch (e: Exception) {
                Log.e("CommentViewModel", "Fail", e)
            }
        }

    }

    suspend fun createComment(commentData: CommentData): Boolean {
        return try {
            CommentService.createComment(commentData)
            true
        } catch (e: Exception) {
            Log.e("CommentViewModel", "Fail", e)
            false
        }
    }

    suspend fun deleteComment(commentId: Long) {
        try {
            CommentService.deleteComment(commentId)

        } catch (e: Exception) {
            Log.e("CommentViewModel", "Fail", e)

        }
    }
}
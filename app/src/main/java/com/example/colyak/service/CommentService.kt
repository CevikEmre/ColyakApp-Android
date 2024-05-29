package com.example.colyak.service

import android.util.Log
import com.example.colyak.`interface`.CommentInterface
import com.example.colyak.model.Comment
import com.example.colyak.model.data.CommentData
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class CommentService {
    companion object {
        suspend fun getCommentsById(receiptId: Long): List<Comment>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(CommentInterface::class.java)
                        .getCommentsById(receiptId)
                        .awaitResponse()
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "CommentService",
                            "getCommentsById request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("CommentService", "Error sending getCommentsById request", e)
                    null
                }
            }
        }

        suspend fun createComment(commentData: CommentData) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(CommentInterface::class.java)
                        .createComment(commentData)
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "CommentService",
                            "createComment request failed with code: $errorCode, message: $errorMessage"
                        )
                    }

                } catch (e: Exception) {
                    Log.e("createComment", "Error sending createComment request", e)

                }
            }
        }

        suspend fun deleteComment(commentId: Long) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(CommentInterface::class.java)
                        .deleteComment(commentId)
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "CommentService",
                            "createComment request failed with code: $errorCode, message: $errorMessage"
                        )
                    }

                } catch (e: Exception) {
                    Log.e("createComment", "Error sending createComment request", e)

                }
            }
        }

        suspend fun updateComment(commentId: Long,comment:String) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(CommentInterface::class.java)
                        .uptadeComment(commentId,comment)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "CommentService",
                            "updateComment request failed with code: $errorCode, message: $errorMessage"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("CommentService", "Error sending updateComment request", e)
                }
            }
        }

    }
}
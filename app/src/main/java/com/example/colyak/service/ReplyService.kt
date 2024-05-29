package com.example.colyak.service

import android.util.Log
import com.example.colyak.`interface`.ReplyInterface
import com.example.colyak.model.CommentRepliesResponse
import com.example.colyak.model.ReplyResponse
import com.example.colyak.model.data.ReplyData
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ReplyService {
    suspend fun getRepliesById(commentId: Long): List<ReplyResponse?>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(ReplyInterface::class.java)
                    .getCommentRepliesById(commentId)
                    .awaitResponse()
                if (response.isSuccessful) {
                    response.body()
                } else {

                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e(
                        "ReplyService",
                        "getRepliesById request failed with code: $errorCode, message: $errorMessage"
                    )
                    null
                }

            } catch (e: Exception) {
                Log.e("ReplyService", "Error sending getRepliesById request", e)
                null
            }
        }
    }

    suspend fun createReply(replyData: ReplyData) {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(ReplyInterface::class.java)
                    .createReply(replyData)
                if (response.isSuccessful) {
                    response.body()
                    Log.e("asd", response.toString())
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e(
                        "ReplyService",
                        "createReply request failed with code: $errorCode, message: $errorMessage"
                    )
                }

            } catch (e: Exception) {
                Log.e("ReplyService", "Error sending createReply request", e)

            }
        }
    }

    suspend fun getCommentsRepliesByReceiptId(receiptId: Long): List<CommentRepliesResponse?>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(ReplyInterface::class.java)
                    .getCommentRepliesByReceiptId(receiptId)
                    .awaitResponse()
                if (response.isSuccessful) {
                    response.body()
                } else {

                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e(
                        "ReplyService",
                        "getRepliesById request failed with code: $errorCode, message: $errorMessage"
                    )
                    null
                }

            } catch (e: Exception) {
                Log.e("ReplyService", "Error sending getRepliesById request", e)
                null
            }
        }
    }

    suspend fun deleteReply(replyId: Long) {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(ReplyInterface::class.java)
                    .deleteReply(replyId)
                if (response.isSuccessful) {
                    response.body()
                    Log.e("asd", response.toString())
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e(
                        "ReplyService",
                        "createReply request failed with code: $errorCode, message: $errorMessage"
                    )
                }

            } catch (e: Exception) {
                Log.e("ReplyService", "Error sending createReply request", e)

            }
        }
    }

    suspend fun updateReply(replyId: Long, newReply: String) {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(ReplyInterface::class.java)
                    .updateReply(replyId,newReply)
                if (response.isSuccessful) {
                    response.body()
                    Log.e("asd", response.toString())
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e(
                        "ReplyService",
                        "createReply request failed with code: $errorCode, message: $errorMessage"
                    )
                }

            } catch (e: Exception) {
                Log.e("ReplyService", "Error sending createReply request", e)

            }
        }
    }
}
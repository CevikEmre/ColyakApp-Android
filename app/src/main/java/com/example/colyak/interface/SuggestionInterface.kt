package com.example.colyak.`interface`

import com.example.colyak.model.data.SuggestionData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SuggestionInterface {

    @POST("/api/suggestions/add")
    suspend fun addSuggestion(@Body suggestionData: SuggestionData):Response<Void>
}
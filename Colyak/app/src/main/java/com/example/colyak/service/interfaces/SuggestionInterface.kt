package com.example.colyak.service.interfaces

import com.example.colyak.model.data.SuggestionData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SuggestionInterface {

    @POST("/api/suggestion/add")
    suspend fun addSuggestion(@Body suggestionData: SuggestionData):Response<Void>
}
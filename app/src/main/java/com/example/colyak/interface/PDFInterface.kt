package com.example.colyak.`interface`

import com.example.colyak.model.PDFResponse
import retrofit2.Call
import retrofit2.http.GET

interface PDFInterface {
    @GET("api/image/get/pdfListData2")
    fun getPDF(): Call<List<PDFResponse>>

}
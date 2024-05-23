package com.example.colyak.`interface`

import com.example.colyak.model.Barcode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BarcodeInterface {
    @GET("api/barcodes/code/{code}")
    fun getBarcode(@Path("code") code: String): Call<Barcode?>
}
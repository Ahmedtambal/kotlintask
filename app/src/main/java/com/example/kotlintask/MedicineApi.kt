package com.example.kotlintask

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MedicineApi {
    @GET("cf1c4e6f-4bf2-4765-8626-5e0d66a4b301")
    suspend fun getMedicines(): List<Medicine>

    companion object {
        private const val BASE_URL = "https://mocky.io/v3/"  // Replace with your base URL

        fun create(): MedicineApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MedicineApi::class.java)
        }
    }
}

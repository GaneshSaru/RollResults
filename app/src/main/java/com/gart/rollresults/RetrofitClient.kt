package com.gart.rollresults

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
     private const val BASE_URL ="http://10.0.2.2:5000" // Emulator IP
   // private const val BASE_URL ="flask-fwcv9i8m0-ganesh-sarus-projects.vercel.app" // Vercel URL

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
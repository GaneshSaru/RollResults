package com.gart.rollresults

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class RollNoRequest(val roll_no: String, val semester: String)

interface ApiService {
    @POST("/get_result")

    fun getResult(@Body rollNoRequest: RollNoRequest): Call<Map<String, String>>

    @GET("get_semesters")
    fun getSemesters(): Call<Map<String,List<String>>>
}
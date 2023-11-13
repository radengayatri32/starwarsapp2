package com.example.appstarwars

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("people")
    fun getResponse(): Call<Responses>
}

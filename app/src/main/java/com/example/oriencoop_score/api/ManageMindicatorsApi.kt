package com.example.oriencoop_score.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ManageMindicatorsApi {
    private const val BASE_URL = "http://172.20.0.57:8080/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val mindicators = retrofit.create(MindicatorInterface::class.java)
}

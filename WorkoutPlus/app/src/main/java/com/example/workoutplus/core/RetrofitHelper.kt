package com.example.workoutplus.core

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit{
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()

        return Retrofit.Builder()
            .baseUrl("https://workoutplusapi-fjg5eacucjcxcpbp.spaincentral-01.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
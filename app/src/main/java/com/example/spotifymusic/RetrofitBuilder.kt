package com.example.spotifymusic

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface RetrofitBuilder {
    companion object{
    fun getInstance(baseUrl:String):Retrofit {
        return  Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    }
}
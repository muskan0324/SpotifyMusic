package com.example.spotifymusic.apiService

import com.example.spotifymusic.RetrofitBuilder
import com.example.spotifymusic.dto.response.SpotifyTrackDetailsResponse
import com.example.spotifymusic.dto.response.SpotifyTrendingSongsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyAPIService {

    @GET("top_200_tracks")
    suspend fun getTrendingSongs(@Query("country") country:String="",@Query("date" )date:String ="2023-07-13",
                         @Header("X-RapidAPI-Key")    apiKey : String="5b15b447ecmsh2af6fe7db0a8779p1238b7jsne22ab87e0926",
                         @Header("X-RapidAPI-Host")   apiHost:String ="spotify81.p.rapidapi.com")
                        :List<SpotifyTrendingSongsResponse>

    @GET("tracks")
    suspend fun getTrackDetails(@Query("ids") trackId:String,
                                @Header("X-RapidAPI-Key")    apiKey : String="5b15b447ecmsh2af6fe7db0a8779p1238b7jsne22ab87e0926",
                                @Header("X-RapidAPI-Host")   apiHost:String ="spotify81.p.rapidapi.com")
                        : SpotifyTrackDetailsResponse


    companion object{
        private const val baseUrl="https://spotify81.p.rapidapi.com/"
        var apiService:SpotifyAPIService?=null

        fun getInstance():SpotifyAPIService{
            if(apiService==null){
                apiService= RetrofitBuilder.getInstance(baseUrl).
                create(SpotifyAPIService::class.java)

//               apiService= RetrofitBuilder.Builder()
//                    .baseUrl(baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build().create(SpotifyAPIService::class.java)
            }
            return apiService!!
        }

    }
}
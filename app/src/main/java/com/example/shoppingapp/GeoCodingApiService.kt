package com.example.shoppingapp

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApiService  {
    @GET("maps/api/geocode/json")
    suspend fun getAddress(
        @Query("latlng") latlng:String,
        @Query("key") apiKey:String
    ):GeocodingResponse
}
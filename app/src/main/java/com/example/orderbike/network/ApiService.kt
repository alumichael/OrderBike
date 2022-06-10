package com.example.orderbike.network

import com.example.orderbike.model.BikeModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("map_service.html?mtype=pub_transport&co=stacje_rowerowe")
    suspend fun getBikeItem() : BikeModel

    companion object {
        var apiService: ApiService? = null
        fun getInstance() : ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("http://www.poznan.pl/mim/plan/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}
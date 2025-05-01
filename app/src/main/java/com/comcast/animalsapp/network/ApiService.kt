package com.comcast.animalsapp.network

import com.comcast.animalsapp.model.Animal
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("animals")
    suspend fun getAnimals(@Query("name") name: String): List<Animal>
}
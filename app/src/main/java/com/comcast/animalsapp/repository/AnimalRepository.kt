package com.comcast.animalsapp.repository

import com.comcast.animalsapp.model.Animal
import com.comcast.animalsapp.network.ApiService

class AnimalRepository(private val api: ApiService) {
    private val cache = mutableMapOf<String, Pair<Long, List<Animal>>>()
    private val ttl = 10 * 60 * 1000L // 10 minutes in millis

    suspend fun getAnimals(name: String): List<Animal> {
        val currentTime = System.currentTimeMillis()
        val cached = cache[name]
        if (cached != null && currentTime - cached.first < ttl) return cached.second
        val result = api.getAnimals(name)
        cache[name] = currentTime to result
        return result
    }
}
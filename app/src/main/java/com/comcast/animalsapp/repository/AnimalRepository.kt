package com.comcast.animalsapp.repository

import com.comcast.animalsapp.model.Animal
import com.comcast.animalsapp.network.ApiService

class AnimalRepository(private val api: ApiService) {

    // In-memory cache: maps search name to a pair of (timestamp, list of animals)
    private val cache = mutableMapOf<String, Pair<Long, List<Animal>>>()

    // Time-to-live for cache entries: 10 minutes (in milliseconds)
    private val ttl = 10 * 60 * 1000L

    /**
     * Fetches animals from cache or API based on the provided name.
     * Results are cached for 10 minutes to reduce redundant API calls.
     *
     * @param name The name to search animals by (e.g., "dog", "bird").
     * @return A list of Animal objects.
     */
    suspend fun getAnimals(name: String): List<Animal> {
        val currentTime = System.currentTimeMillis()
        val cached = cache[name]

        // Return cached data if it's still valid (within TTL)
        if (cached != null && currentTime - cached.first < ttl) {
            return cached.second
        }

        // Fetch fresh data from API if not cached or expired
        val result = api.getAnimals(name)

        // Update cache with current time and new result
        cache[name] = currentTime to result

        return result
    }
}

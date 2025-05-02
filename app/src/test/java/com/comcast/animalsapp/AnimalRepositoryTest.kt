package com.comcast.animalsapp

import com.comcast.animalsapp.model.Animal
import com.comcast.animalsapp.model.Characteristics
import com.comcast.animalsapp.model.Taxonomy
import com.comcast.animalsapp.network.ApiService
import com.comcast.animalsapp.repository.AnimalRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class AnimalRepositoryTest {

    private val api = mockk<ApiService>()
    private lateinit var repository: AnimalRepository

    @Before
    fun setup() {
        repository = AnimalRepository(api)
    }

    @Test
    fun `should return cached data within ttl`() = runTest {
        val fakeAnimal = Animal("Dog", Taxonomy("Chordata", "Canis lupus"), Characteristics("Dog"))
        coEvery { api.getAnimals("dog") } returns listOf(fakeAnimal)

        val firstCall = repository.getAnimals("dog")
        val secondCall = repository.getAnimals("dog")

        assertEquals(firstCall, secondCall)
        coVerify(exactly = 1) { api.getAnimals("dog") }
    }
}

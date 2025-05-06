package com.comcast.animalsapp

import com.comcast.animalsapp.model.Animal
import com.comcast.animalsapp.model.Characteristics
import com.comcast.animalsapp.model.Taxonomy
import com.comcast.animalsapp.repository.AnimalRepository
import com.comcast.animalsapp.viewmodel.AnimalViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimalViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AnimalViewModel
    private val mockRepo = mockk<AnimalRepository>()

    private val fakeAnimal = Animal(
        name = "TestDog",
        taxonomy = Taxonomy(
            phylum = "Chordata",
            scientific_name = "Canis lupus"
        ),
        characteristics = Characteristics(common_name = "Doggo"),
        type = "dog"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { mockRepo.getAnimals("dog") } returns List(3) { fakeAnimal.copy(name = "Dog$it") }
        coEvery { mockRepo.getAnimals("bird") } returns List(3) { fakeAnimal.copy(name = "Bird$it", type = "bird") }
        coEvery { mockRepo.getAnimals("bug") } returns List(3) { fakeAnimal.copy(name = "Bug$it", type = "bug") }

        viewModel = AnimalViewModel(mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAnimals populates animals correctly`() = runTest(testDispatcher) {
        viewModel.loadAnimals()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(9, viewModel.animals.value.size)
        assert(viewModel.animals.value.all { it.type in listOf("dog", "bird", "bug") })
    }

    @Test
    fun `loadAnimals sets errorMessage on failure`() = runTest(testDispatcher) {
        coEvery { mockRepo.getAnimals("dog") } throws RuntimeException("API Error")

        viewModel.loadAnimals()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Failed to load animals. Please check your network or try again later.", viewModel.errorMessage.value)
        assertEquals(0, viewModel.animals.value.size)
    }


}

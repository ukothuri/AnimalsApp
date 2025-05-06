package com.comcast.animalsapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comcast.animalsapp.model.Animal
import com.comcast.animalsapp.repository.AnimalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimalViewModel(private val repository: AnimalRepository) : ViewModel() {

    // Holds the complete list of animals (dog, bird, bug) after fetching from the API
    private val _animals = MutableStateFlow<List<Animal>>(emptyList())
    val animals = _animals.asStateFlow() // public immutable access

    // Stores the current search input from the user
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Updates the search query state when user types in the search box
    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    // Filters animals in real-time based on the search query and emits the filtered list
    val filteredAnimals = combine(animals, searchQuery) { list, query ->
        // Regex to match whole words, e.g., 'bird' won't match 'ladybug'
        val pattern = "\\b${Regex.escape(query)}\\b".toRegex(RegexOption.IGNORE_CASE)

        list.filter {
            val name = it.name
            val commonName = it.characteristics.common_name
            val type = it.type

            // Log the values being checked and the result
            val matches = pattern.containsMatchIn(name) ||
                    pattern.containsMatchIn(commonName) ||
                    pattern.containsMatchIn(type)

            Log.d("AnimalFilter", "name='$name', commonName='$commonName', type='$type', pattern='${pattern.pattern}', matches=$matches")
           matches
        }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Loads animal data from th repository, assigns a type tag to each group, and stores the combined list
    fun loadAnimals() {
        viewModelScope.launch {
            // Fetch the first 3 dog entries from the API, and tag each with type = "dog"
            val dogList = repository.getAnimals("dog").take(3).map { it.copy(type = "dog") }
            // Fetch the first 3 bird entries and tag each with type = "bird"
            val birdList = repository.getAnimals("bird").take(3).map { it.copy(type = "bird") }
            // Fetch the first 3 bug entries and tag each with type = "bug"
            val bugList = repository.getAnimals("bug").take(3).map { it.copy(type = "bug") }

            // Combine the tagged animal lists into a single list and update the state
            // This ensures the UI can display a unified, clearly-typed list of animals
            _animals.value = dogList + birdList + bugList
        }
    }
}

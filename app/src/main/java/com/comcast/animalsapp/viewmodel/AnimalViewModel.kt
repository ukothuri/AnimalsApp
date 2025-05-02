package com.comcast.animalsapp.viewmodel

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
            pattern.containsMatchIn(it.name.orEmpty()) ||                            // Match in name
                    pattern.containsMatchIn(it.characteristics.common_name.orEmpty()) ||     // Match in common name
                    pattern.containsMatchIn(it.type.orEmpty())                               // Match in assigned type ("dog", "bird", "bug")
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Loads animal data from th repository, assigns a type tag to each group, and stores the combined list
    fun loadAnimals() {
        viewModelScope.launch {
            // Fetch and tag 3 animals per group
            val dogList = repository.getAnimals("dog").take(3).map { it.copy(type = "dog") }
            val birdList = repository.getAnimals("bird").take(3).map { it.copy(type = "bird") }
            val bugList = repository.getAnimals("bug").take(3).map { it.copy(type = "bug") }

            // Combine all into a single list and update the state
            _animals.value = dogList + birdList + bugList
        }
    }
}

package com.comcast.animalsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comcast.animalsapp.model.Animal
import com.comcast.animalsapp.repository.AnimalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimalViewModel(private val repository: AnimalRepository) : ViewModel() {
    private val _animals = MutableStateFlow<List<Animal>>(emptyList())
    val animals = _animals.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    fun updateSearch(query: String) {
        searchQuery = query
    }

    val filteredAnimals = animals.map { list ->
        list.filter {
            it.name.contains(searchQuery, true) ||
                    it.characteristics.common_name.contains(searchQuery, true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun loadAnimals() {
        viewModelScope.launch {
            val dogList = repository.getAnimals("dog")
            val birdList = repository.getAnimals("bird")
            val bugList = repository.getAnimals("bug")
            _animals.value = (dogList + birdList + bugList).take(9) // 3 of each assumed
        }
    }
}
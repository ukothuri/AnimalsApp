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
    private val _animals = MutableStateFlow<List<Animal>>(emptyList())
    val animals = _animals.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    val filteredAnimals = combine(animals, searchQuery) { list, query ->
        val pattern = "\\b${Regex.escape(query)}\\b".toRegex(RegexOption.IGNORE_CASE)
        list.filter {
            pattern.containsMatchIn(it.name.orEmpty()) ||
                    pattern.containsMatchIn(it.characteristics.common_name.orEmpty()) ||
                    pattern.containsMatchIn(it.type.orEmpty())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    fun loadAnimals() {
        viewModelScope.launch {
            val dogList = repository.getAnimals("dog").take(3).map { it.copy(type = "dog") }
            val birdList = repository.getAnimals("bird").take(3).map { it.copy(type = "bird") }
            val bugList = repository.getAnimals("bug").take(3).map { it.copy(type = "bug") }
            _animals.value = dogList + birdList + bugList
        }
    }

}
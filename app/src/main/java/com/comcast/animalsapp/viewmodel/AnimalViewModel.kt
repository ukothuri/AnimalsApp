package com.comcast.animalsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comcast.animalsapp.domain.usecase.GetAnimalsUseCase
import com.comcast.animalsapp.ui.state.AnimalUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalViewModel @Inject constructor(
    private val getAnimalsUseCase: GetAnimalsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimalUiState>(AnimalUiState.Loading)
    val uiState: StateFlow<AnimalUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    val filteredAnimals = combine(_uiState, searchQuery) { state, query ->
        if (state is AnimalUiState.Success) {
            val pattern = "\\b${Regex.escape(query)}\\b".toRegex(RegexOption.IGNORE_CASE)
            state.animals.filter {
                val name = it.name
                val commonName = it.characteristics.common_name
                val type = it.type
                pattern.containsMatchIn(name) ||
                        pattern.containsMatchIn(commonName) ||
                        pattern.containsMatchIn(type)
            }
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun loadAnimals() {
        viewModelScope.launch {
            _uiState.value = AnimalUiState.Loading
            try {
                val dogList = getAnimalsUseCase("dog", 2)
                val birdList = getAnimalsUseCase("bird")
                val bugList = getAnimalsUseCase("bug")
                val allAnimals = dogList + birdList + bugList
                _uiState.value = AnimalUiState.Success(allAnimals)
            } catch (e: Exception) {
                println("AnimalViewModel: ${e.message}")
                _uiState.value = AnimalUiState.Error("Failed to load animals. Please check your network or try again later.")
            }
        }
    }
}

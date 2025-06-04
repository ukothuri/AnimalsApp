package com.comcast.animalsapp.ui.state

import com.comcast.animalsapp.model.Animal

/**
 * Represents the different UI states for the animal screen.
 */
sealed class AnimalUiState {
    object Loading : AnimalUiState()
    data class Success(val animals: List<Animal>) : AnimalUiState()
    data class Error(val message: String) : AnimalUiState()
}



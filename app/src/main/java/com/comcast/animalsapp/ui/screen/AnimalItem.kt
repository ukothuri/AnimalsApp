package com.comcast.animalsapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.comcast.animalsapp.model.Animal

/**
 * A composable that displays details about a given [Animal].
 * Additional information is conditionally shown based on the animal type.
 */
@Composable
fun AnimalItem(animal: Animal) {
    // Use a vertical layout with padding around the content
    Column(Modifier.padding(8.dp)) {

        // Display the animal's basic taxonomy information
        Text("Name: ${animal.name}")
        Text("Phylum: ${animal.taxonomy.phylum}")
        Text("Scientific: ${animal.taxonomy.scientific_name}")

        // Conditionally display additional characteristics based on the animal's name
        when {
            // If the animal's name includes "dog", show slogan and lifespan
            animal.name.contains("dog", ignoreCase = true) -> {
                animal.characteristics.slogan?.let { Text("Slogan: $it") }
                animal.characteristics.lifespan?.let { Text("Lifespan: $it") }
            }

            // If the animal's name includes "bird", show wingspan and habitat
            animal.name.contains("bird", ignoreCase = true) -> {
                animal.characteristics.wingspan?.let { Text("Wingspan: $it") }
                animal.characteristics.habitat?.let { Text("Habitat: $it") }
            }

            // If the animal's name includes "bug", show prey and predator information
            animal.name.contains("bug", ignoreCase = true) -> {
                animal.characteristics.prey?.let { Text("Prey: $it") }
                animal.characteristics.predators?.let { Text("Predators: $it") }
            }
        }
    }
}

package com.comcast.animalsapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.comcast.animalsapp.model.Animal

@Composable
fun AnimalItem(animal: Animal) {
    Column(Modifier.padding(8.dp)) {
        Text("Name: ${animal.name}")
        Text("Phylum: ${animal.taxonomy.phylum}")
        Text("Scientific: ${animal.taxonomy.scientific_name}")

        when {
            animal.name.contains("dog", ignoreCase = true) -> {
                animal.characteristics.slogan?.let { Text("Slogan: $it") }
                animal.characteristics.lifespan?.let { Text("Lifespan: $it") }
            }
            animal.name.contains("bird", ignoreCase = true) -> {
                animal.characteristics.wingspan?.let { Text("Wingspan: $it") }
                animal.characteristics.habitat?.let { Text("Habitat: $it") }
            }
            animal.name.contains("bug", ignoreCase = true) -> {
                animal.characteristics.prey?.let { Text("Prey: $it") }
                animal.characteristics.predators?.let { Text("Predators: $it") }
            }
        }
    }
}

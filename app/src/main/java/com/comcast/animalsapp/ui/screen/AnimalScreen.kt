package com.comcast.animalsapp.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.comcast.animalsapp.viewmodel.AnimalViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

@Composable
fun AnimalScreen(viewModel: AnimalViewModel) {
    val orientation = LocalConfiguration.current.orientation
    val animalList by viewModel.filteredAnimals.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Search Animals") }
        )

        errorMessage?.let { error ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error, color = androidx.compose.ui.graphics.Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.loadAnimals() }) {
                    Text("Retry")
                }
            }
        }

        if (animalList.isNotEmpty()) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(animalList) { animal ->
                        AnimalItem(animal)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(animalList) { animal ->
                        AnimalItem(animal)
                    }
                }
            }
        }
    }
}
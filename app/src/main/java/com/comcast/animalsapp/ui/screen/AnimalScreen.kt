package com.comcast.animalsapp.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.comcast.animalsapp.ui.state.AnimalUiState
import com.comcast.animalsapp.viewmodel.AnimalViewModel

@Composable
fun AnimalScreen(viewModel: AnimalViewModel) {
    val orientation = LocalConfiguration.current.orientation
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredList by viewModel.filteredAnimals.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Search Animals") }
        )

        when (uiState) {
            is AnimalUiState.Loading -> {
                Text(
                    text = "Loading animals...",
                    modifier = Modifier.padding(16.dp)
                )
            }

            is AnimalUiState.Error -> {
                val errorMessage = (uiState as AnimalUiState.Error).message
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = errorMessage)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(onClick = { viewModel.loadAnimals() }) {
                        Text("Retry")
                    }
                }
            }

            is AnimalUiState.Success -> {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    LazyRow(
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredList) { animal ->
                            AnimalItem(animal)
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredList) { animal ->
                            AnimalItem(animal)
                        }
                    }
                }
            }
        }
    }
}

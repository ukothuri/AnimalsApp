package com.comcast.animalsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.comcast.animalsapp.network.RetrofitClient
import com.comcast.animalsapp.repository.AnimalRepository
import com.comcast.animalsapp.ui.screen.AnimalScreen
import com.comcast.animalsapp.ui.theme.AnimalsAppTheme
import com.comcast.animalsapp.viewmodel.AnimalViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = AnimalViewModel(AnimalRepository(RetrofitClient.api))
        viewModel.loadAnimals()
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AnimalScreen(viewModel)
            }
        }
    }
}

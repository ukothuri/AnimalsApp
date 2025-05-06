package com.comcast.animalsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.comcast.animalsapp.ui.screen.AnimalScreen
import com.comcast.animalsapp.ui.theme.AnimalsAppTheme
import com.comcast.animalsapp.viewmodel.AnimalViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Hilt-injected ViewModel
    private val viewModel: AnimalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadAnimals()

        setContent {
            AnimalsAppTheme {
                AnimalScreen(viewModel = viewModel)
            }
        }
    }
}
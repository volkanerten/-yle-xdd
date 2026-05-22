package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainAppScreen
import com.example.ui.screens.Screen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ServiceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ServiceViewModel = viewModel()
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

                    // Sistem geri tuşuna basıldığında ana ekrana dön
                    BackHandler(enabled = currentScreen != Screen.Dashboard && currentScreen != Screen.Splash) {
                        currentScreen = Screen.Dashboard
                    }

                    MainAppScreen(
                        viewModel = viewModel,
                        currentScreen = currentScreen,
                        onNavigate = { screen ->
                            currentScreen = screen
                        }
                    )
                }
            }
        }
    }
}

package com.henan.wisdom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.henan.wisdom.core.ui.theme.HenanWisdomTheme
import com.henan.wisdom.ui.screens.HomeScreen
import com.henan.wisdom.ui.screens.StudyScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HenanWisdomTheme {
                var currentScreen by remember { mutableStateOf("home") }
                
                Surface(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        "home" -> HomeScreen(onNavigateToStudy = { currentScreen = "study" })
                        "study" -> StudyScreen(onStudyComplete = { currentScreen = "home" })
                    }
                }
            }
        }
    }
}

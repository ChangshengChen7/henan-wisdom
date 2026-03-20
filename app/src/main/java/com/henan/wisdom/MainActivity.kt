package com.henan.wisdom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.henan.wisdom.core.ui.theme.SmarterHenanTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主Activity
 * 智慧河南 - 河南省知识学习系统
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            SmarterHenanTheme {
                var selectedRoute by remember { mutableStateOf("home") }
                
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedRoute == "home",
                                onClick = { selectedRoute = "home" },
                                icon = { Text("🏠") },
                                label = { Text("首页") }
                            )
                            NavigationBarItem(
                                selected = selectedRoute == "study",
                                onClick = { selectedRoute = "study" },
                                icon = { Text("📚") },
                                label = { Text("学习") }
                            )
                            NavigationBarItem(
                                selected = selectedRoute == "progress",
                                onClick = { selectedRoute = "progress" },
                                icon = { Text("📊") },
                                label = { Text("进度") }
                            )
                            NavigationBarItem(
                                selected = selectedRoute == "settings",
                                onClick = { selectedRoute = "settings" },
                                icon = { Text("⚙️") },
                                label = { Text("设置") }
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedRoute) {
                            "home" -> HomeScreen()
                            "study" -> StudyScreen()
                            "progress" -> ProgressScreen()
                            "settings" -> SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "智慧河南",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "河南省知识学习系统",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StudyScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("学习页面 - 开发中", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun ProgressScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("进度页面 - 开发中", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("设置页面 - 开发中", style = MaterialTheme.typography.headlineSmall)
    }
}
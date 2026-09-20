package com.norskallstars

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.norskallstars.ui.learning.LearningScreen
import com.norskallstars.ui.main.MainScreen

@Composable
fun NorskAllstarsNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onStartLearning = { navController.navigate("learning") },
                onAchievements = { /* TODO */ },
                onSettings = { /* TODO */ }
            )
        }
        composable("learning") {
            LearningScreen(onBack = { navController.popBackStack() })
        }
    }
}
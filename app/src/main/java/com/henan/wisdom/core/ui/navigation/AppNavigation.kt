package com.henan.wisdom.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.henan.wisdom.core.ui.screens.HomeScreen
import com.henan.wisdom.core.ui.screens.ProgressScreen
import com.henan.wisdom.core.ui.screens.StudyScreen
import com.henan.wisdom.core.ui.screens.SettingsScreen
import com.henan.wisdom.core.ui.components.KnowledgeCard
import com.henan.wisdom.core.ui.components.RatingLevel

/**
 * 导航路由
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Study : Screen("study")
    object Progress : Screen("progress")
    object Settings : Screen("settings")
}

/**
 * 应用导航
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 首页
        composable(Screen.Home.route) {
            HomeScreen(
                todayProgress = 3,
                totalGoal = 10,
                streakDays = 7,
                learnedToday = 3,
                onStartStudy = {
                    navController.navigate(Screen.Study.route)
                },
                onViewProgress = {
                    navController.navigate(Screen.Progress.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        // 学习页
        composable(Screen.Study.route) {
            StudyScreen(
                currentCard = KnowledgeCard(
                    id = 1,
                    title = "河南省的省会",
                    content = "郑州市",
                    category = "省情",
                    difficulty = "简单"
                ),
                currentIndex = 0,
                totalCards = 10,
                isFlipped = false,
                onFlip = { },
                onRatingSelected = { rating ->
                    // 处理评分
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // 进度页
        composable(Screen.Progress.route) {
            ProgressScreen(
                totalCards = 100,
                learnedCards = 45,
                streakDays = 7,
                totalReviews = 200,
                todayStudyTime = 1800000,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // 设置页
        composable(Screen.Settings.route) {
            SettingsScreen(
                dailyGoal = 10,
                reminderEnabled = true,
                reminderTime = "08:00",
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

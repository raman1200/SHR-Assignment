package com.project.raman.shr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.raman.shr.presentation.home.DashboardScreen
import com.project.raman.shr.presentation.home.FeedScreen
import com.project.raman.shr.presentation.home.LeaderboardScreen
import com.project.raman.shr.presentation.home.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = HomeDest.Dashboard.route) {

        composable(HomeDest.Dashboard.route) {
            DashboardScreen()
        }

        composable(HomeDest.Feed.route) {
            FeedScreen()
        }

        composable(HomeDest.Leaderboard.route) {
            LeaderboardScreen()
        }

        composable(HomeDest.Settings.route) {
            SettingsScreen()
        }
    }
}

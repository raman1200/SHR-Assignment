package com.project.raman.shr.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector



sealed class HomeDest(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : HomeDest("dashboard", "Dashboard", Icons.Default.Home)
    object Feed : HomeDest("feed", "Feed", Icons.Default.MailOutline)
    object Leaderboard : HomeDest("leaderboard", "Leaderboard", Icons.AutoMirrored.Filled.List)
    object Settings : HomeDest("settings", "Settings", Icons.Default.Settings)

}



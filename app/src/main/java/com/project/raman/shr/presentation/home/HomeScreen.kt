package com.project.raman.shr.presentation.home

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.project.raman.shr.presentation.auth.AuthViewModel
import com.project.raman.shr.presentation.auth.LoginActivity
import com.project.raman.shr.presentation.navigation.AppNavHost
import com.project.raman.shr.presentation.navigation.HomeDest
import com.project.raman.shr.ui.theme.SHRTheme
import com.project.raman.shr.utils.PrefKeys
import com.project.raman.shr.utils.PrefManager

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,)
@Composable
fun PreviewHome() {
    SHRTheme {
        HomeScreen()
    }
}
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val items = listOf(
        HomeDest.Dashboard,
        HomeDest.Feed,
        HomeDest.Leaderboard,
        HomeDest.Settings
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {} ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Chat Support",
                        modifier = Modifier.size(28.dp)
                    )
                    Text("Need help")
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 4.dp
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.outline,
                            unselectedTextColor = MaterialTheme.colorScheme.outline,

                        ),
                        icon = { Icon(screen.icon, contentDescription = screen.route) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            AppNavHost(navController)
        }
    }
}

// Dashboard Screen
@Composable
fun DashboardScreen() {
    var selectedPeriod by remember { mutableStateOf("Weekly") }
    val periods = listOf("Weekly", "Monthly")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Sustainability Report",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                periods.forEach { period ->
                    FilterChip(
                        selected = selectedPeriod == period,
                        onClick = { selectedPeriod = period },
                        label = { Text(period) }
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Total CO2e Savings",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "127.5 kg",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "↑ 23% from last $selectedPeriod",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCCFFCC)
                    )
                }
            }
        }

        item {
            Text(
                "Key Metrics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Transport",
                    value = "42.3 kg",
                    icon = Icons.Filled.DirectionsBus,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Food",
                    value = "35.8 kg",
                    icon = Icons.Filled.Restaurant,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Shopping",
                    value = "28.4 kg",
                    icon = Icons.Filled.ShoppingBag,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Energy",
                    value = "21.0 kg",
                    icon = Icons.Filled.Bolt,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Activity Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ActivityBar("Public Transport", 35, Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(8.dp))
                    ActivityBar("Plant-based Meals", 28, Color(0xFF66BB6A))
                    Spacer(modifier = Modifier.height(8.dp))
                    ActivityBar("Sustainable Shopping", 22, Color(0xFF81C784))
                    Spacer(modifier = Modifier.height(8.dp))
                    ActivityBar("Energy Saving", 15, Color(0xFF9CCC65))
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ActivityBar(label: String, percentage: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("$percentage%", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = percentage / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color
        )
    }
}

// Feed Screen
data class FeedPost(
    val id: Int,
    val userName: String,
    val action: String,
    val category: String,
    val co2Saved: String,
    val timeAgo: String,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean = false
)

@Composable
fun FeedScreen() {
    val posts = remember {
        mutableStateListOf(
            FeedPost(1, "Sarah Chen", "Took the bus instead of driving", "Transport", "2.3 kg", "2h ago", 24, 5),
            FeedPost(2, "Mike Johnson", "Had a plant-based lunch", "Food", "1.8 kg", "4h ago", 18, 3),
            FeedPost(3, "Emma Davis", "Bought second-hand clothing", "Shopping", "4.2 kg", "6h ago", 32, 8),
            FeedPost(4, "Alex Kumar", "Cycled to work today", "Transport", "3.5 kg", "1d ago", 45, 12),
            FeedPost(5, "Lisa Park", "Zero-waste grocery shopping", "Shopping", "2.1 kg", "1d ago", 28, 6)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Share your sustainable action today!", fontWeight = FontWeight.Medium)
                }
            }
        }

        items(posts) { post ->
            FeedPostCard(
                post = post,
                onLike = { id ->
                    val index = posts.indexOfFirst { it.id == id }
                    if (index != -1) {
                        val currentPost = posts[index]
                        posts[index] = currentPost.copy(
                            isLiked = !currentPost.isLiked,
                            likes = if (currentPost.isLiked) currentPost.likes - 1 else currentPost.likes + 1
                        )
                    }
                },
                onComment = { }
            )
        }
    }
}

@Composable
fun FeedPostCard(post: FeedPost, onLike: (Int) -> Unit, onComment: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E7D32)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        post.userName.first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(post.userName, fontWeight = FontWeight.Bold)
                    Text(post.timeAgo, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(post.action, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(post.category) },
                    leadingIcon = {
                        Icon(
                            when (post.category) {
                                "Transport" -> Icons.Filled.DirectionsBus
                                "Food" -> Icons.Filled.Restaurant
                                else -> Icons.Filled.ShoppingBag
                            },
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                AssistChip(
                    onClick = { },
                    label = { Text("${post.co2Saved} CO2e saved") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    onClick = { onLike(post.id) }
                ) {
                    Icon(
                        if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) Color.Red else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${post.likes}")
                }

                TextButton(
                    onClick = { onComment(post.id) }
                ) {
                    Icon(
                        Icons.Filled.ChatBubbleOutline,
                        contentDescription = "Comment",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${post.comments}")
                }

                TextButton(onClick = { }) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share")
                }
            }
        }
    }
}

// Leaderboard Screen
data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val score: Int,
    val co2Saved: String,
    val isCurrentUser: Boolean = false
)

@Composable
fun LeaderboardScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Weekly", "Monthly", "All Time")

    val users = listOf(
        LeaderboardUser(1, "Emma Davis", 2450, "156.3 kg"),
        LeaderboardUser(2, "Alex Kumar", 2380, "148.7 kg"),
        LeaderboardUser(3, "Sarah Chen", 2210, "137.5 kg"),
        LeaderboardUser(4, "You", 2150, "127.5 kg", true),
        LeaderboardUser(5, "Mike Johnson", 2080, "122.4 kg"),
        LeaderboardUser(6, "Lisa Park", 1950, "115.8 kg"),
        LeaderboardUser(7, "Tom Wilson", 1890, "110.2 kg"),
        LeaderboardUser(8, "Anna Lee", 1820, "105.6 kg")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            Column {
                Text(
                    "Leaderboard",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                LeaderboardCard(user)
            }
        }
    }
}

@Composable
fun LeaderboardCard(user: LeaderboardUser) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (user.isCurrentUser) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (user.isCurrentUser) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (user.rank) {
                            1 -> Color(0xFFFFD700)
                            2 -> Color(0xFFC0C0C0)
                            3 -> Color(0xFFCD7F32)
                            else -> Color(0xFF2E7D32)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    user.rank.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    user.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "${user.co2Saved} CO2e saved",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${user.score}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    "points",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Settings Screen
@Composable
fun SettingsScreen() {
    var userName by remember { mutableStateOf(PrefManager.getString(PrefKeys.USER_PROFILE_NAME, "Unknown")) }
    var profileUrl by remember { mutableStateOf(PrefManager.getString(PrefKeys.USER_PROFILE_URL)) }
    var email by remember { mutableStateOf(PrefManager.getString(PrefKeys.UER_EMAIL)) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    val ctx = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // load the image later
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2E7D32)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userName.first().toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        enabled = false,
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        enabled = false,
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        item {
            Text(
                "Preferences",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Notifications", fontWeight = FontWeight.Medium)
                            Text(
                                "Get updates on your progress",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Dark Mode", fontWeight = FontWeight.Medium)
                            Text(
                                "Switch to dark theme",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it }
                        )
                    }
                }
            }
        }

        item {
            Text(
                "Sustainability Goals",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GoalItem("Use public transport 3x/week", true)
                    GoalItem("2 plant-based meals/day", true)
                    GoalItem("Zero waste shopping", false)
                    GoalItem("Reduce energy by 20%", false)
                }
            }
        }

        item {
            OutlinedButton(
                onClick = {
                    FirebaseAuth.getInstance().signOut() // Later this will shift to viewmodel
                    ctx.startActivity(Intent(ctx, LoginActivity::class.java))
                    PrefManager.clear()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Text("Log Out")
            }
        }
    }
}

@Composable
fun GoalItem(goal: String, isActive: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (isActive) Icons.Filled.CheckCircle else Icons.Filled.Circle,
                contentDescription = null,
                tint = if (isActive) Color(0xFF2E7D32) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(goal)
        }
    }
}
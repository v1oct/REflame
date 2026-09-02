package com.currupt.reflame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/**
 * Centralized route definitions for CURRUPT. Studio.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Projects : Screen("projects")
    object ProjectDetails : Screen("projects/{slug}") {
        fun createRoute(slug: String) = "projects/$slug"
    }
    object Releases : Screen("releases")
    object About : Screen("about")
    object Admin : Screen("admin")
}

@Composable
fun CorruptNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            PlaceholderScreen("CURRUPT. Home")
        }

        composable(Screen.Projects.route) {
            PlaceholderScreen("Projects Catalog")
        }

        composable(Screen.ProjectDetails.route) { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug") ?: ""
            PlaceholderScreen("Project Details: $slug")
        }

        composable(Screen.Releases.route) {
            PlaceholderScreen("Studio Releases")
        }

        composable(Screen.About.route) {
            PlaceholderScreen("About CURRUPT.")
        }

        composable(Screen.Admin.route) {
            PlaceholderScreen("Admin Dashboard (Authorized Only)")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$name Placeholder",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

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
 * Centralized route definitions for RΞflame.
 */
sealed class Screen(val route: String) {
    object Hub : Screen("hub")
    object Reading : Screen("reading")
    object TitleDetails : Screen("details/{titleId}") {
        fun createRoute(titleId: String) = "details/$titleId"
    }
    object Reader : Screen("reader/{titleId}/{chapterId}") {
        fun createRoute(titleId: String, chapterId: String) = "reader/$titleId/$chapterId"
    }
    object Library : Screen("library")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

@Composable
fun ReflameNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Hub.route,
        modifier = modifier
    ) {
        composable(Screen.Hub.route) {
            HubScreen { categoryTitle ->
                when (categoryTitle) {
                    "Movies" -> navController.navigate(Screen.Reading.route)
                    "Music" -> navController.navigate(Screen.Library.route)
                    "Anime" -> navController.navigate(Screen.Search.route)
                    "Manhwa" -> navController.navigate(Screen.Reading.route)
                }
            }
        }

        composable(Screen.Reading.route) {
            ReadingHomeScreen(
                onBackClick = { navController.popBackStack() },
                onTitleClick = { titleId ->
                    navController.navigate(Screen.TitleDetails.createRoute(titleId))
                }
            )
        }

        composable(Screen.TitleDetails.route) { backStackEntry ->
            val titleId = backStackEntry.arguments?.getString("titleId") ?: ""
            TitleDetailsScreen(
                titleId = titleId,
                onBackClick = { navController.popBackStack() },
                onTitleClick = { id ->
                    navController.navigate(Screen.TitleDetails.createRoute(id))
                },
                onReadClick = { tId, cId ->
                    navController.navigate(Screen.Reader.createRoute(tId, cId))
                }
            )
        }

        composable(Screen.Reader.route) { backStackEntry ->
            val titleId = backStackEntry.arguments?.getString("titleId") ?: ""
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
            ReaderScreen(
                titleId = titleId,
                chapterId = chapterId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Library.route) { PlaceholderScreen("Library") }
        composable(Screen.Search.route) { PlaceholderScreen("Search") }
        composable(Screen.Profile.route) { PlaceholderScreen("Profile") }
        composable(Screen.Settings.route) { PlaceholderScreen("Settings") }
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
            text = "$name Screen Placeholder",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

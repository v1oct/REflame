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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.currupt.reflame.feature.music.MusicHomeViewModel
import com.currupt.reflame.feature.reading.ReadingHomeViewModel
import com.currupt.reflame.feature.reading.TitleDetailsViewModel

/**
 * Centralized route definitions for RΞflame.
 */
sealed class Screen(val route: String) {
    object Hub : Screen("hub")
    object Reading : Screen("reading")
    object Music : Screen("music")
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
                    "Music" -> navController.navigate(Screen.Music.route)
                    "Manhwa" -> navController.navigate(Screen.Reading.route)
                    else -> {} // Removed Anime/Movies
                }
            }
        }

        composable(Screen.Reading.route) {
            val homeViewModel: ReadingHomeViewModel = viewModel()
            ReadingHomeScreen(
                onBackClick = { navController.popBackStack() },
                onTitleClick = { titleId ->
                    navController.navigate(Screen.TitleDetails.createRoute(titleId))
                },
                viewModel = homeViewModel
            )
        }

        composable(Screen.TitleDetails.route) { backStackEntry ->
            val titleId = backStackEntry.arguments?.getString("titleId") ?: ""
            val detailsViewModel: TitleDetailsViewModel = viewModel(
                key = titleId,
                factory = TitleDetailsViewModel.provideFactory(titleId)
            )
            
            TitleDetailsScreen(
                titleId = titleId,
                onBackClick = { navController.popBackStack() },
                onTitleClick = { id ->
                    navController.navigate(Screen.TitleDetails.createRoute(id))
                },
                onReadClick = { tId, cId ->
                    navController.navigate(Screen.Reader.createRoute(tId, cId))
                },
                viewModel = detailsViewModel
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

        composable(Screen.Music.route) {
            val musicViewModel: MusicHomeViewModel = viewModel()
            // Placeholder Music Screen UI
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                Text(text = "Music Vertical Placeholder", color = Color.White)
            }
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

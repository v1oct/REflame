package com.currupt.reflame

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
import com.currupt.reflame.feature.about.AboutScreen
import com.currupt.reflame.feature.admin.AdminScreen
import com.currupt.reflame.feature.home.HomeScreen
import com.currupt.reflame.feature.projects.ProjectDetailsScreen
import com.currupt.reflame.feature.projects.ProjectsScreen
import com.currupt.reflame.feature.releases.ReleasesScreen
import com.currupt.reflame.ui.component.AppShell

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
    AppShell(navController = navController) { _ ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onContentClick = { slug ->
                        navController.navigate(Screen.ProjectDetails.createRoute(slug))
                    }
                )
            }

            composable(Screen.Projects.route) {
                ProjectsScreen(
                    onContentClick = { slug ->
                        navController.navigate(Screen.ProjectDetails.createRoute(slug))
                    }
                )
            }

            composable(Screen.ProjectDetails.route) { backStackEntry ->
                val slug = backStackEntry.arguments?.getString("slug") ?: ""
                ProjectDetailsScreen(
                    slug = slug,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Releases.route) {
                ReleasesScreen()
            }

            composable(Screen.About.route) {
                AboutScreen()
            }

            composable(Screen.Admin.route) {
                AdminScreen()
            }
        }
    }
}

package com.currupt.reflame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
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
    AppShell(navController = navController) { innerPadding ->
        val layoutDirection = LocalLayoutDirection.current
        
        // We apply top padding to keep content below the persistent top bar.
        // Bottom padding is handled by individual screens to allow content to scroll behind the floating nav.
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateStartPadding(layoutDirection),
                end = innerPadding.calculateEndPadding(layoutDirection)
            )
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

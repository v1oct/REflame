package com.currupt.reflame.ui.component

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.currupt.reflame.Screen
import kotlinx.coroutines.launch

@Composable
fun AppShell(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topLevelRoutes = listOf(
        Screen.Home.route,
        Screen.Projects.route,
        Screen.Releases.route,
        Screen.About.route,
        Screen.Admin.route
    )

    val isTopLevel = currentRoute in topLevelRoutes

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Black,
                drawerShape = RoundedCornerShape(0.dp),
                modifier = Modifier.width(320.dp),
                drawerTonalElevation = 0.dp
            ) {
                DrawerContent(
                    currentRoute = currentRoute,
                    onDestinationClick = { route ->
                        scope.launch { drawerState.close() }
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        },
        scrimColor = Color.Black.copy(alpha = 0.8f),
        gesturesEnabled = isTopLevel
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
            topBar = {
                if (isTopLevel) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .clickable { scope.launch { drawerState.open() } },
                            color = Color.White.copy(alpha = 0.04f)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Box(modifier = Modifier.size(width = 18.dp, height = 1.5.dp).background(Color.White))
                                    Box(modifier = Modifier.size(width = 12.dp, height = 1.5.dp).background(Color.White))
                                    Box(modifier = Modifier.size(width = 18.dp, height = 1.5.dp).background(Color.White))
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                if (isTopLevel) {
                    FloatingBottomNav(navController)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                AmbientBackground()
                
                content(innerPadding)
            }
        }
    }
}

@Composable
private fun DrawerContent(
    currentRoute: String?,
    onDestinationClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        
        Text(
            text = "CURRUPT.",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = Color.White
            )
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Primary Navigation
        DrawerItem("HOME", Screen.Home.route, Icons.Rounded.Home, currentRoute == Screen.Home.route, onDestinationClick)
        DrawerItem("CATALOG", Screen.Projects.route, Icons.Rounded.Dashboard, currentRoute == Screen.Projects.route, onDestinationClick)
        DrawerItem("RELEASES", Screen.Releases.route, Icons.Rounded.NewReleases, currentRoute == Screen.Releases.route, onDestinationClick)
        DrawerItem("ABOUT", Screen.About.route, Icons.Rounded.Info, currentRoute == Screen.About.route, onDestinationClick)
        
        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
        Spacer(modifier = Modifier.height(32.dp))
        
        // Studio Section
        Text(
            text = "STUDIO",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.4f),
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem("Announcements", Screen.Home.route, null, false, onDestinationClick)
        DrawerItem("Development", Screen.Home.route, null, false, onDestinationClick)
        DrawerItem("Media", Screen.Home.route, null, false, onDestinationClick)
        
        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
        Spacer(modifier = Modifier.height(32.dp))
        
        // Admin Section
        DrawerItem("ADMIN", Screen.Admin.route, Icons.Rounded.AdminPanelSettings, currentRoute == Screen.Admin.route, onDestinationClick)
        
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
private fun DrawerItem(
    label: String,
    route: String,
    icon: ImageVector?,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(route) },
        color = if (isSelected) Color.White.copy(alpha = 0.08f) else Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            } else {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Composable
fun FloatingBottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        NavigationItem("HOME", Screen.Home.route, Icons.Rounded.Home),
        NavigationItem("PROJECTS", Screen.Projects.route, Icons.Rounded.RocketLaunch),
        NavigationItem("RELEASES", Screen.Releases.route, Icons.Rounded.NewReleases),
        NavigationItem("ABOUT", Screen.About.route, Icons.Rounded.Info)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(32.dp))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(32.dp)),
            color = Color.Black.copy(alpha = 0.7f),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                            }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(24.dp)
                        )
                        AnimatedVisibility(visible = isSelected) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(4.dp)
                                    .background(Color.White, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

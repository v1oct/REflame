package com.currupt.reflame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.currupt.reflame.ui.component.LaunchScreen
import com.currupt.reflame.ui.theme.RΞTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RΞTheme(darkTheme = true) {
                var showLaunch by remember { mutableStateOf(true) }
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    if (showLaunch) {
                        LaunchScreen(onComplete = { showLaunch = false })
                    } else {
                        val navController = rememberNavController()
                        CorruptNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

package com.example.pupistore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pupistore.ui.theme.PupiStoreTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkThemeFlow = getTheme(this@MainActivity)
            val isDarkTheme by isDarkThemeFlow.collectAsState(initial = false)
            val coroutineScope = rememberCoroutineScope()
            PupiStoreTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("registro") { FormularioApp(navController) }
                    composable("login") {
                        LoginScreen(navController, isDarkTheme) {
                            coroutineScope.launch {
                                saveTheme(!isDarkTheme, this@MainActivity)
                            }
                        }
                    }
                    composable("home/{username}") { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        HomeScreen(navController, username, isDarkTheme) {
                            coroutineScope.launch {
                                saveTheme(!isDarkTheme, this@MainActivity)
                            }
                        }
                    }
                    composable("theme_settings") {
                        ThemeSettingsScreen()
                    }
                }
            }
        }
    }
}

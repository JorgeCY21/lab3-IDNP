package com.example.pupistore

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    username: String,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit
) {
    val navBarController = rememberNavController()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PupiStore - $username") },
                actions = {
                    IconButton(onClick = onThemeChange) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.DarkMode,
                            contentDescription = "Cambiar tema"
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            if (!isLandscape) BottomNavigationBar(navBarController)
        }
    ) { padding ->
        NavigationContent(
            navBarController,
            username,
            Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Inicio", "inicio", Icons.Filled.Home),
        BottomNavItem("Buscar", "buscar", Icons.Filled.Search),
        BottomNavItem("Favoritos", "favoritos", Icons.Filled.Favorite),
        BottomNavItem("Carrito", "carrito", Icons.Filled.ShoppingCart),
        BottomNavItem("Perfil", "perfil", Icons.Filled.Person)
    )
    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun NavigationContent(navController: NavHostController, username: String, modifier: Modifier) {
    NavHost(navController = navController, startDestination = "inicio", modifier = modifier) {
        composable("inicio") { ProductListScreen(username) }
        composable("buscar") { EmptyScreen("Buscar") }
        composable("favoritos") { EmptyScreen("Favoritos") }
        composable("carrito") { EmptyScreen("Carrito") }
        composable("perfil") { EmptyScreen("Perfil") }
    }
}

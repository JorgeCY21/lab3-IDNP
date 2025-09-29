package com.example.pupistore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.pupistore.ui.theme.PupiStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PupiStoreTheme(darkTheme = false, dynamicColor = false) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") { LoginScreen(navController) }
                    composable("home/{username}") { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        HomeScreen(navController, username)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PupiStore - Iniciar Sesión") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Cualquier usuario/contraseña funciona
                    navController.navigate("home/$username")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, username: String) {
    val navBarController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navBarController, username = username, rootNav = navController)
        }
    ) { padding ->
        NavHost(
            navController = navBarController,
            startDestination = "inicio",
            modifier = Modifier.padding(padding)
        ) {
            composable("inicio") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Bienvenido $username a PupiStore 🐱", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Opciones de compra: Juguetes, Camas, Collares, Comida 😺")
                }
            }
            composable("perfil") {
                ProfileScreen(rootNav = navController)
            }
            // Otros tabs vacíos
            composable("buscar") { EmptyScreen("Buscar") }
            composable("favoritos") { EmptyScreen("Favoritos") }
            composable("carrito") { EmptyScreen("Carrito") }
        }
    }
}

@Composable
fun ProfileScreen(rootNav: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Este es tu perfil 🐾", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                // Al cerrar sesión volvemos al login
                rootNav.navigate("login") {
                    popUpTo(rootNav.graph.findStartDestination().id) { inclusive = true }
                }
            }
        ) {
            Text("Cerrar Sesión")
        }
    }
}

@Composable
fun EmptyScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("$title (no implementado)")
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, username: String, rootNav: NavHostController) {
    val items = listOf(
        BottomNavItem("Inicio", "inicio", Icons.Default.Home),
        BottomNavItem("Buscar", "buscar", Icons.Default.Search),
        BottomNavItem("Favoritos", "favoritos", Icons.Default.Favorite),
        BottomNavItem("Carrito", "carrito", Icons.Default.ShoppingCart),
        BottomNavItem("Perfil", "perfil", Icons.Default.Person)
    )

    NavigationBar {
        val currentDestination = navController.currentDestination
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

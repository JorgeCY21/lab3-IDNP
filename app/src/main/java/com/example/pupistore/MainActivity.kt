package com.example.pupistore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.pupistore.ui.theme.PupiStoreTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            PupiStoreTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "registro") {
                        composable("registro") {
                            FormularioApp(navController)
                        }
                        composable("login") {
                            LoginScreen(navController, isDarkTheme) { isDarkTheme = !isDarkTheme }
                        }
                        composable("home/{username}") { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: ""
                            HomeScreen(navController, username, isDarkTheme) { isDarkTheme = !isDarkTheme }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text("PupiStore - Iniciar Sesión") },
                actions = {
                    IconButton(onClick = onThemeChange) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.DarkMode,
                            contentDescription = "Cambiar tema"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(if (isLandscape) 32.dp else 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a PupiStore 🐾",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate("home/$username") },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Icon(Icons.Filled.Login, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ingresar")
            }
        }
    }
}

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
fun NavigationContent(navController: NavHostController, username: String, modifier: Modifier) {
    NavHost(navController = navController, startDestination = "inicio", modifier = modifier) {
        composable("inicio") { CircleAnimationScreen(username) }
        composable("buscar") { EmptyScreen("Buscar") }
        composable("favoritos") { EmptyScreen("Favoritos") }
        composable("carrito") { EmptyScreen("Carrito") }
        composable("perfil") { EmptyScreen("Perfil") }
    }
}

@Composable
fun CircleAnimationScreen(username: String) {
    var isProcessing by remember { mutableStateOf(false) }
    var processCompleted by remember { mutableStateOf(false) }
    val circleSize by animateDpAsState(targetValue = if (isProcessing) 160.dp else 80.dp)
    val circleColor = when {
        processCompleted -> Color(0xFF4CAF50)
        isProcessing -> Color(0xFF6C63FF)
        else -> Color(0xFF9E9E9E)
    }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Hola $username 👋", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))
        Canvas(modifier = Modifier.size(circleSize)) {
            drawCircle(color = circleColor)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (!isProcessing && !processCompleted) {
                    scope.launch {
                        isProcessing = true
                        delay(3000)
                        isProcessing = false
                        processCompleted = true
                    }
                } else if (processCompleted) {
                    processCompleted = false
                }
            },
            enabled = !isProcessing,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            when {
                isProcessing -> Text("Procesando...")
                processCompleted -> Text("Reiniciar proceso")
                else -> Text("Iniciar proceso")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when {
            isProcessing -> Text("Cargando información del sistema...", color = Color(0xFF6C63FF))
            processCompleted -> Text("Proceso completado ✅", color = Color(0xFF4CAF50))
            else -> Text("Listo para comenzar 🔹", color = Color.Gray)
        }
    }
}

@Composable
fun FormularioApp(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("Masculino") }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableStateOf(50f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing) // 🔹 Evita que choque con la barra superior
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Registro en PupiStore",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(top = 12.dp, bottom = 16.dp)
        )

        Text("Nombre:")
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Ingrese su nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Contraseña:")
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Ingrese su contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
            Text("Acepto los términos y condiciones")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text("Género:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender == "Masculino",
                    onClick = { selectedGender = "Masculino" }
                )
                Text("Masculino")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender == "Femenino",
                    onClick = { selectedGender = "Femenino" }
                )
                Text("Femenino")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            Text("  Habilitar notificaciones")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nivel de satisfacción: ${sliderValue.toInt()}")
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..100f,
            steps = 5
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && password.isNotBlank() && isChecked) {
                    navController.navigate("login")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar y Continuar")
        }
    }
}


@Composable
fun EmptyScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("$title (no implementado)")
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
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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

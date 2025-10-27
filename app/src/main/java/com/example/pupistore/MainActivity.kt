package com.example.pupistore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
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
                    NavHost(navController = navController, startDestination = "login") {
                        composable("registro") { FormularioApp(navController) }
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
                text = "Bienvenido a PupiStore 🐱",
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
                onClick = {
                    // Validación simple: no permitir campos vacíos
                    if (username.isNotBlank() && password.isNotBlank()) {
                        // Navegar y limpiar stack para que el usuario no vuelva al login con back
                        navController.navigate("home/$username") {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Icon(Icons.Filled.Login, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ingresar")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { navController.navigate("registro") }) {
                Text("¿No tienes cuenta? Regístrate")
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
                    IconButton(
                        onClick = {
                            // Cerrar sesión: navegar a login y limpiar backstack para evitar volver con back
                            navController.navigate("login") {
                                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
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
fun NavigationContent(navController: NavHostController, username: String, modifier: Modifier) {
    NavHost(navController = navController, startDestination = "inicio", modifier = modifier) {
        composable("inicio") { ProductListScreen(username) }
        composable("buscar") { EmptyScreen("Buscar") }
        composable("favoritos") { EmptyScreen("Favoritos") }
        composable("carrito") { EmptyScreen("Carrito") }
        composable("perfil") { EmptyScreen("Perfil") }
    }
}

@Composable
fun ProductListScreen(username: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Encabezado simple
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(8.dp))
            Column {
                Text("Tiendita de Pupi 🐱", style = MaterialTheme.typography.titleMedium)
                Text("Productos recomendados para tu gatito", color = Color.Gray)
            }
        }

        // Lista con 20 productos reales para gatos
        val productos = remember {
            listOf(
                Producto("Arena aglomerante para gatos - 5 kg", 39.90, "Control de olores y fácil limpieza"),
                Producto("Comida seca para gatitos 1.5 kg", 45.50, "Formulada para crecimiento y energía"),
                Producto("Comida húmeda (lata) - sabor atún", 4.20, "Rica en proteínas, ideal como complemento"),
                Producto("Snack dental para gatos - 60 g", 12.00, "Cuida la higiene bucal"),
                Producto("Rascador poste (mediano)", 79.90, "Evita daños en muebles"),
                Producto("Cama para gato - tamaño pequeño", 55.00, "Acolchada y lavable"),
                Producto("Juguete pluma con varita", 9.50, "Estimulación y ejercicio"),
                Producto("Arenero cerrado con tapa", 129.99, "Mayor privacidad y menos olor"),
                Producto("Bol comedero antideslizante", 19.90, "Ideal para agua y comida"),
                Producto("Cepillo para pelo corto", 14.50, "Reduce pelo suelto y bolas de pelo"),
                Producto("Transportadora para gatos (pequeña)", 89.90, "Segura y cómoda para viajes"),
                Producto("Collar con campana ajustable", 8.50, "Identificación y seguridad"),
                Producto("Fuente de agua automática 2L", 129.00, "Agua fresca constante"),
                Producto("Arena desechable para viajes (pack)", 24.90, "Uso temporal y práctico"),
                Producto("Spray repelente para muebles", 18.00, "Protege tus muebles favoritos"),
                Producto("Caja de arena biodegradable - 10L", 49.90, "Ecológica y absorbente"),
                Producto("Pasta de malta para bolas de pelo", 11.90, "Facilita expulsión de bolas de pelo"),
                Producto("Cinturón arnés para paseo", 39.00, "Paseos cortos con seguridad"),
                Producto("Cascada de juguete interactivo", 59.90, "Entretenimiento durante horas"),
                Producto("Kit de aseo básico (tijeras + cortaúñas)", 22.50, "Mantenimiento en casa")
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = null,
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(producto.nombre, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("S/ ${"%.2f".format(producto.precio)}", color = Color(0xFF2E7D32))
                            Spacer(Modifier.height(2.dp))
                            Text(producto.descripcion, color = Color.Gray, maxLines = 2)
                        }
                    }
                }
            }
        }
    }
}

data class Producto(val nombre: String, val precio: Double, val descripcion: String)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioApp(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro en PupiStore", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 12.dp, bottom = 16.dp))
        TextField(value = name, onValueChange = { name = it }, label = { Text("Ingrese su nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Ingrese su contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
            Text("Acepto los términos y condiciones")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { if (name.isNotBlank() && password.isNotBlank() && isChecked) navController.navigate("login") }, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Enviar y Continuar")
        }
    }
}

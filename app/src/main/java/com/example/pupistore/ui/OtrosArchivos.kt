package com.example.pupistore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProductListScreen(username: String) {
    Column(modifier = Modifier.fillMaxSize()) {
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

        val productos = listOf(
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

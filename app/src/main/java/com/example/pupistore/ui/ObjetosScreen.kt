package com.example.pupistore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pupistore.data.provideDB
import com.example.pupistore.viewmodel.ObjetoViewModel
import com.example.pupistore.data.Objeto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjetosScreen() {
    val context = LocalContext.current
    val db = remember { provideDB(context) }
    val vm = remember { ObjetoViewModel(db) }

    val lista by vm.lista.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Registrar objeto", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank()) {
                    vm.insertar(nombre.trim(), descripcion.trim())
                    nombre = ""
                    descripcion = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Spacer(Modifier.height(20.dp))

        Text("Lista de objetos", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))

        if (lista.isEmpty()) {
            Text("No hay objetos guardados", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lista) { item: Objeto ->
                    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text(item.descripcion, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

package com.example.pupistore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pupistore.data.AppDatabase
import com.example.pupistore.data.Objeto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ObjetoViewModel(private val db: AppDatabase) : ViewModel() {

    private val _lista = MutableStateFlow<List<Objeto>>(emptyList())
    val lista = _lista.asStateFlow()

    init { cargar() }

    fun insertar(nombre: String, descripcion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.objetoDao().insertar(Objeto(nombre = nombre, descripcion = descripcion))
            cargar()
        }
    }

    private fun cargar() {
        viewModelScope.launch(Dispatchers.IO) {
            _lista.value = db.objetoDao().obtenerTodos()
        }
    }
}

package com.example.almondinimes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.almondinimes.models.Usuario
import com.example.almondinimes.models.ObraGuardada

class AmigosViewModel : ViewModel() {

    private val _listaAmigos = MutableLiveData<MutableList<Usuario>>(mutableListOf())
    val listaAmigos: LiveData<MutableList<Usuario>> get() = _listaAmigos

    // Funciones vacías para que el resto de la app no dé error al compilar
    fun buscarUsuarios(query: String): List<Usuario> = emptyList()
    
    fun getListaPorUsuario(idUsuario: Int, tipo: String): List<ObraGuardada> = emptyList()

    fun getAmigosDeUsuario(idUsuario: Int): List<Usuario> = emptyList()

    fun añadirAmigos(nuevos: Set<Usuario>) {}

    fun borrarAmigo(idParaBorrar: Int) {}
}

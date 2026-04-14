package com.example.almondinimes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.almondinimes.models.ObraGuardada
import com.example.almondinimes.models.Usuario

class AmigosViewModel : ViewModel() {

    // Simulación de "base de datos" de todos los usuarios registrados
    private val todosLosUsuarios = listOf(
        Usuario("Almondi", 1234),
        Usuario("Nimes", 5678),
        Usuario("Goku", 9000),
        Usuario("Luffy", 1997),
        Usuario("Naruto", 1999),
        Usuario("Zoro", 3333),
        Usuario("Nami", 4444)
    )

    // La lista de amigos que el usuario actual ya ha añadido
    private val _listaAmigos = MutableLiveData<MutableList<Usuario>>(mutableListOf())
    val listaAmigos: LiveData<MutableList<Usuario>> get() = _listaAmigos

    // Map que asocia cada ID de usuario con su lista de obras (anime/manga)
    private val _listasPorUsuario = MutableLiveData<Map<Int, List<ObraGuardada>>>(
        mapOf(
            1234 to listOf(
                ObraGuardada(21, "One Piece", 9.5, "Viendo", "https://cdn.myanimelist.net/images/anime/1208/94745.jpg", "ANIME"),
                ObraGuardada(20, "Naruto", 8.0, "Completado", "https://cdn.myanimelist.net/images/anime/13/17405.jpg", "ANIME"),
                ObraGuardada(2, "Berserk", 10.0, "Leyendo", "https://cdn.myanimelist.net/images/manga/1/157897.jpg", "MANGA")
            ),
            5678 to listOf(
                ObraGuardada(16498, "Attack on Titan", 9.0, "Completado", "https://cdn.myanimelist.net/images/anime/10/47347.jpg", "ANIME"),
                ObraGuardada(1535, "Death Note", 8.5, "Completado", "https://cdn.myanimelist.net/images/anime/9/9453.jpg", "ANIME")
            ),
            9000 to listOf(
                ObraGuardada(813, "Dragon Ball Z", 10.0, "Completado", "https://cdn.myanimelist.net/images/anime/6/20936.jpg", "ANIME")
            )
        )
    )

    // Map que asocia cada ID de usuario con su lista de amigos (IDs)
    private val _amigosPorUsuario = mapOf(
        1234 to listOf(5678, 9000), // Almondi es amigo de Nimes y Goku
        5678 to listOf(1234),       // Nimes es amigo de Almondi
        9000 to listOf(1234, 1997)  // Goku es amigo de Almondi y Luffy
    )

    // Función para buscar usuarios registrados para poder añadirlos
    fun buscarUsuarios(query: String): List<Usuario> {
        if (query.isEmpty()) return emptyList()
        return todosLosUsuarios.filter { 
            it.nick.contains(query, ignoreCase = true) || it.idNumerico.toString().contains(query)
        }
    }

    // Funciones para obtener información de un usuario específico
    fun getListaPorUsuario(idUsuario: Int, tipo: String): List<ObraGuardada> {
        val todas = _listasPorUsuario.value?.get(idUsuario) ?: emptyList()
        return todas.filter { it.type == tipo }
    }

    fun getAmigosDeUsuario(idUsuario: Int): List<Usuario> {
        val idsAmigos = _amigosPorUsuario[idUsuario] ?: emptyList()
        return todosLosUsuarios.filter { idsAmigos.contains(it.idNumerico) }
    }

    // Función para añadir varios amigos a la lista del usuario actual
    fun añadirAmigos(nuevos: Set<Usuario>) {
        val listaActual = _listaAmigos.value ?: mutableListOf()
        nuevos.forEach { nuevo ->
            if (!listaActual.any { it.idNumerico == nuevo.idNumerico }) {
                listaActual.add(nuevo)
            }
        }
        _listaAmigos.value = listaActual
    }

    // Función para borrar un amigo de la lista del usuario actual
    fun borrarAmigo(idParaBorrar: Int) {
        val listaActual = _listaAmigos.value ?: mutableListOf()
        listaActual.removeAll { it.idNumerico == idParaBorrar }
        _listaAmigos.value = listaActual
    }
}
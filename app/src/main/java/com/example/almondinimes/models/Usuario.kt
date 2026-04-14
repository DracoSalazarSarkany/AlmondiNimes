package com.example.almondinimes.models

data class Usuario(
    val nick: String,
    val idNumerico: Int,
    val fotoUrl: String? = null,
    var isSelected: Boolean = false // Útil para el diálogo de añadir
) {
    // Propiedad para obtener el formato Nick#00001 automáticamente
    val fullId: String get() = String.format("%s#%05d", nick, idNumerico)
}

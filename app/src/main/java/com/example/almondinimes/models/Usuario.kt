package com.example.almondinimes.models

data class Usuario(
    val uid: String = "",
    val nick: String = "",
    val email: String = "",
    val idNumerico: Int = 0,
    val fotoUrl: String? = null,
    val age: Int = 0,
    val birthDate: String = "",
    var isSelected: Boolean = false
) {
    // Propiedad para obtener el formato Nick#00001 automáticamente
    val fullId: String get() = String.format("%s#%05d", nick, idNumerico)
}

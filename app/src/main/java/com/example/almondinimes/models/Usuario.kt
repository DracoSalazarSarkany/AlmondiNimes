package com.example.almondinimes.models

import com.google.firebase.firestore.Exclude

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
    // Evitamos que se guarde en Firestore para que siempre se calcule con el nick actual
    @get:Exclude
    val fullId: String get() = String.format("%s#%05d", nick, idNumerico)
}

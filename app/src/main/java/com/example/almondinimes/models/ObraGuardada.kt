package com.example.almondinimes.models

data class ObraGuardada(
    val malId: Int,
    val title: String,
    var score: Double? = null, // null significa "Sin puntuar"
    var status: String = "Pendiente",
    val imageUrl: String,
    val type: String // "ANIME" o "MANGA"
)

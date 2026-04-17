package com.example.almondinimes.models

data class ObraGuardada(
    val malId: Int = 0,
    val title: String = "",
    var score: Double? = null, // null significa "Sin puntuar" (S/P)
    var status: String = "Pendiente",
    val imageUrl: String = "",
    val type: String = "" // "ANIME" o "MANGA"
)

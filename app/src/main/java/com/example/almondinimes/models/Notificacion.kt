package com.example.almondinimes.models

import java.util.Date

data class Notificacion(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val fecha: Date = Date(),
    var leida: Boolean = false,
    val tipo: TipoNotificacion = TipoNotificacion.INFO
)



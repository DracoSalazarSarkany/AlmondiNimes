package com.example.almondinimes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.almondinimes.models.Notificacion
import com.example.almondinimes.models.TipoNotificacion
import java.util.Date

class NotificacionesViewModel : ViewModel() {
    private val _notificaciones = MutableLiveData<MutableList<Notificacion>>()
    val notificaciones: MutableLiveData<MutableList<Notificacion>> get() = _notificaciones

    init {
        // Datos de ejemplo para probar
        _notificaciones.value = mutableListOf(
            Notificacion("1", "¡Bienvenido a AlmondiNimes!", "Gracias por unirte a nuestra comunidad. Empieza añadiendo tus animes favoritos.", Date(), false, TipoNotificacion.WELCOME),
            Notificacion("2", "Nueva Versión 1.1", "Hemos añadido la posibilidad de copiar listas de tus amigos. ¡Pruébalo!", Date(), true, TipoNotificacion.UPDATE)
        )
    }

    fun marcarComoLeida(notificacion: Notificacion) {
        val lista = _notificaciones.value ?: return
        val index = lista.indexOfFirst { it.id == notificacion.id }
        if (index != -1 && !lista[index].leida) {
            lista[index].leida = true
            _notificaciones.value = lista // Dispara el observador
        }
    }
}
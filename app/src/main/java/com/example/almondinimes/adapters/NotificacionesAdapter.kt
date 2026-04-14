package com.example.almondinimes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.models.Notificacion
import com.example.almondinimes.models.TipoNotificacion
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Locale

class NotificacionesAdapter(
    private var notificaciones: List<Notificacion>,
    private val onNotificacionClick: (Notificacion) -> Unit
) : RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder>() {

    class NotificacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.card_notificacion)
        val ivTipo: ImageView = view.findViewById(R.id.iv_tipo_notif)
        val tvTitulo: TextView = view.findViewById(R.id.tv_notif_titulo)
        val tvMensaje: TextView = view.findViewById(R.id.tv_notif_mensaje)
        val tvFecha: TextView = view.findViewById(R.id.tv_notif_fecha)
        val viewUnread: View = view.findViewById(R.id.view_unread_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notificacion, parent, false)
        return NotificacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        val notif = notificaciones[position]
        
        holder.tvTitulo.text = notif.titulo
        holder.tvMensaje.text = notif.mensaje
        
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
        holder.tvFecha.text = sdf.format(notif.fecha)

        // Cambiar icono según tipo
        val iconRes = when(notif.tipo) {
            TipoNotificacion.WELCOME -> R.drawable.ic_menu_vector // Podrías añadir más iconos luego
            TipoNotificacion.UPDATE -> R.drawable.ic_menu_vector
            TipoNotificacion.MESSAGE -> R.drawable.ic_menu_vector
            TipoNotificacion.INFO -> R.drawable.ic_menu_vector
        }
        holder.ivTipo.setImageResource(iconRes)

        // Lógica de "Leída"
        if (notif.leida) {
            holder.viewUnread.visibility = View.GONE
            holder.card.strokeColor = 0x22FFFFFF.toInt() // Más tenue
            holder.tvTitulo.alpha = 0.7f
            holder.tvMensaje.alpha = 0.7f
        } else {
            holder.viewUnread.visibility = View.VISIBLE
            holder.card.strokeColor = 0xFF448AFF.toInt() // Borde azul si no está leída
            holder.tvTitulo.alpha = 1.0f
            holder.tvMensaje.alpha = 1.0f
        }

        holder.itemView.setOnClickListener {
            onNotificacionClick(notif)
        }
    }

    override fun getItemCount() = notificaciones.size

    fun updateList(newList: List<Notificacion>) {
        notificaciones = newList
        notifyDataSetChanged()
    }
}

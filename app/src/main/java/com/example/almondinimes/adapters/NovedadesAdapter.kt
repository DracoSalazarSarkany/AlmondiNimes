package com.example.almondinimes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.almondinimes.R
import com.example.almondinimes.models.Anime
import java.text.SimpleDateFormat
import java.util.Locale

class NovedadesAdapter(
    private var listaObras: List<Anime>,
    private val onItemClick: (Anime) -> Unit
) : RecyclerView.Adapter<NovedadesAdapter.NovedadViewHolder>() {

    class NovedadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivObra: ImageView = view.findViewById(R.id.iv_obra)
        val tvTitulo: TextView = view.findViewById(R.id.tv_titulo_obra)
        val tvFecha: TextView = view.findViewById(R.id.tv_fecha_obra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovedadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_noticias, parent, false)
        return NovedadViewHolder(view)
    }

    override fun onBindViewHolder(holder: NovedadViewHolder, position: Int) {
        val obra = listaObras[position]
        holder.tvTitulo.text = obra.title
        
        // Formatear la fecha
        val fechaRaw = obra.aired?.from
        holder.tvFecha.text = if (!fechaRaw.isNullOrEmpty()) {
            "Publicado el ${formatDate(fechaRaw)}"
        } else {
            "Fecha no disponible"
        }

        Glide.with(holder.itemView.context)
            .load(obra.images.jpg.url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.ivObra)

        holder.itemView.setOnClickListener { onItemClick(obra) }
    }

    override fun getItemCount() = listaObras.size

    fun updateData(newList: List<Anime>) {
        val diffCallback = AnimeDiffCallback(this.listaObras, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listaObras = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun addMoreData(newData: List<Anime>) {
        val itemsFiltrados = newData.filter { nuevo ->
            listaObras.none { existente -> existente.mal_id == nuevo.mal_id }
        }
        if (itemsFiltrados.isEmpty()) return

        val startPosition = listaObras.size
        val newList = listaObras.toMutableList()
        newList.addAll(itemsFiltrados)
        this.listaObras = newList
        notifyItemRangeInserted(startPosition, itemsFiltrados.size)
    }

    private fun formatDate(dateStr: String): String {
        return try {
            // Jikan suele enviar: 2024-04-12T00:00:00+00:00
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateStr)
            date?.let { outputFormat.format(it) } ?: dateStr
        } catch (e: Exception) {
            dateStr.split("T")[0] // Fallback: solo la parte de la fecha
        }
    }

    private class AnimeDiffCallback(
        private val oldList: List<Anime>,
        private val newList: List<Anime>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldPosition: Int, newPosition: Int) = oldList[oldPosition].mal_id == newList[newPosition].mal_id
        override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = oldList[oldPosition] == newList[newPosition]
    }
}

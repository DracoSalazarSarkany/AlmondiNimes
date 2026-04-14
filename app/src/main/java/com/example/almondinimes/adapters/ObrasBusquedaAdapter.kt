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

class ObrasBusquedaAdapter(
    private var listaAnimes: List<Anime>,
    private val onItemClick: (Anime) -> Unit
) : RecyclerView.Adapter<ObrasBusquedaAdapter.ObraViewHolder>() {

    class ObraViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCover: ImageView = view.findViewById(R.id.iv_my_anime_cover_obra)
        val tvTitulo: TextView = view.findViewById(R.id.tv_my_anime_title_obra)
        val tvScore: TextView = view.findViewById(R.id.tv_my_anime_score_obra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObraViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_obra, parent, false)
        return ObraViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObraViewHolder, position: Int) {
        val anime = listaAnimes[position]
        holder.tvTitulo.text = anime.title
        
        val score = anime.score ?: 0.0
        holder.tvScore.text = if (score > 0) "⭐ $score" else "⭐ N/A"

        Glide.with(holder.itemView.context)
            .load(anime.images.jpg.url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.ivCover)

        holder.itemView.setOnClickListener { onItemClick(anime) }
    }

    override fun getItemCount() = listaAnimes.size

    fun updateData(newList: List<Anime>) {
        val diffCallback = AnimeDiffCallback(this.listaAnimes, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listaAnimes = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun addMoreData(newData: List<Anime>) {
        val itemsFiltrados = newData.filter { nuevo ->
            listaAnimes.none { existente -> existente.mal_id == nuevo.mal_id }
        }

        if (itemsFiltrados.isEmpty()) return

        val startPosition = listaAnimes.size
        val newList = listaAnimes.toMutableList()
        newList.addAll(itemsFiltrados)
        this.listaAnimes = newList
        notifyItemRangeInserted(startPosition, itemsFiltrados.size)
    }

    private class AnimeDiffCallback(
        private val oldList: List<Anime>,
        private val newList: List<Anime>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition].mal_id == newList[newPosition].mal_id
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition] == newList[newPosition]
        }
    }
}

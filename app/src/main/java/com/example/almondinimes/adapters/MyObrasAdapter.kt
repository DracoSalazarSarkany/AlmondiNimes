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
import com.example.almondinimes.models.ObraGuardada

class MyObrasAdapter(
    private var list: List<ObraGuardada>,
    private val onClick: ((ObraGuardada) -> Unit)? = null
) : RecyclerView.Adapter<MyObrasAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCover: ImageView = view.findViewById(R.id.iv_my_anime_cover)
        val tvTitle: TextView = view.findViewById(R.id.tv_my_anime_title)
        val tvScore: TextView = view.findViewById(R.id.tv_my_anime_score)
        val tvStatus: TextView = view.findViewById(R.id.tv_my_anime_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anime_manga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvTitle.text = item.title
        
        // Formatear nota: si es 10.0 -> 10, si es null -> S/P
        val scoreText = item.score?.let {
            if (it % 1.0 == 0.0) it.toInt().toString() else it.toString()
        } ?: "S/P"
        
        holder.tvScore.text = "⭐ $scoreText"
        holder.tvStatus.text = item.status

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(holder.ivCover)

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<ObraGuardada>) {
        val diffCallback = ObraDiffCallback(this.list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ObraDiffCallback(
        private val oldList: List<ObraGuardada>,
        private val newList: List<ObraGuardada>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition].malId == newList[newPosition].malId
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition] == newList[newPosition]
        }
    }
}

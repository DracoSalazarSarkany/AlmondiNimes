package com.example.almondinimes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.models.Usuario

class AmigosAdapter(
    private var lista: List<Usuario>,
    private val onItemClick: (Usuario) -> Unit
) : RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder>() {

    class AmigoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNickId: TextView = view.findViewById(R.id.tv_friend_nick_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_amigo, parent, false)
        return AmigoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {
        val amigo = lista[position]
        holder.tvNickId.text = amigo.fullId
        holder.itemView.setOnClickListener { onItemClick(amigo) }
    }

    override fun getItemCount() = lista.size

    fun updateData(newList: List<Usuario>) {
        val diffCallback = UsuarioDiffCallback(this.lista, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.lista = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class UsuarioDiffCallback(
        private val oldList: List<Usuario>,
        private val newList: List<Usuario>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition].idNumerico == newList[newPosition].idNumerico
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition] == newList[newPosition]
        }
    }
}
package com.example.almondinimes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.models.Usuario

class BusquedaAmigosAdapter(
    private var listaResultados: List<Usuario>
) : RecyclerView.Adapter<BusquedaAmigosAdapter.BusquedaViewHolder>() {

    // Guardamos los usuarios que el usuario ha marcado con el checkbox
    val seleccionados = mutableSetOf<Usuario>()

    class BusquedaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNickId: TextView = view.findViewById(R.id.tv_search_nick_id)
        val cbAdd: CheckBox = view.findViewById(R.id.cb_add_friend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusquedaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend_search_result, parent, false)
        return BusquedaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusquedaViewHolder, position: Int) {
        val usuario = listaResultados[position]
        holder.tvNickId.text = usuario.fullId
        
        // Resetear el listener para evitar errores al reciclar vistas
        holder.cbAdd.setOnCheckedChangeListener(null)
        holder.cbAdd.isChecked = seleccionados.contains(usuario)

        holder.cbAdd.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) seleccionados.add(usuario) else seleccionados.remove(usuario)
        }
    }

    override fun getItemCount() = listaResultados.size

    fun updateData(newList: List<Usuario>) {
        this.listaResultados = newList
        notifyDataSetChanged()
    }
}
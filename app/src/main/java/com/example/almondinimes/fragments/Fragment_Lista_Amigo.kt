package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.adapters.MyObrasAdapter
import com.example.almondinimes.viewmodels.AmigosViewModel
import com.example.almondinimes.viewmodels.ObrasViewModel

class Fragment_Lista_Amigo : Fragment(R.layout.fragment_friend_item_list) {

    private val amigosViewModel: AmigosViewModel by activityViewModels()
    private val obrasViewModel: ObrasViewModel by activityViewModels()
    private lateinit var adapter: MyObrasAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idUsuario = arguments?.getInt("idUsuario") ?: 0
        val nick = arguments?.getString("nick") ?: "Usuario"
        val tipo = arguments?.getString("tipo") ?: "ANIME"

        val tvTitle = view.findViewById<TextView>(R.id.tv_friend_list_title)
        val rvItems = view.findViewById<RecyclerView>(R.id.rv_friend_items)
        val layoutEmpty = view.findViewById<View>(R.id.layout_empty_friend)

        tvTitle.text = if (tipo == "ANIME") "Animes de $nick" else "Mangas de $nick"

        val listaObras = amigosViewModel.getListaPorUsuario(idUsuario, tipo)
        
        adapter = MyObrasAdapter(listaObras, onClick = { obra ->
            // Al hacer click, vamos al detalle
            val bundle = Bundle().apply {
                putInt("mal_id", obra.malId)
                putString("titulo", obra.title)
                putString("tipo", obra.type) // Pasamos el tipo real (ANIME o MANGA)
                putString("url_imagen", obra.imageUrl)
                // NO pasamos géneros ni sinopsis aquí para que el detalle sepa que debe buscarlos
                putStringArrayList("generos", arrayListOf())
                putString("sinopsis", "Cargando...")
            }
            findNavController().navigate(R.id.action_listaAmigo_to_detalleObra, bundle)
        })
        rvItems.layoutManager = LinearLayoutManager(requireContext())
        rvItems.adapter = adapter

        layoutEmpty.visibility = if (listaObras.isEmpty()) View.VISIBLE else View.GONE
    }
}

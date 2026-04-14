package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.adapters.AmigosAdapter
import com.example.almondinimes.viewmodels.AmigosViewModel

class Fragment_Amigos_De_Amigo : Fragment(R.layout.fragment_amigos) {

    private val viewModel: AmigosViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idUsuario = arguments?.getInt("idUsuario") ?: 0
        val nick = arguments?.getString("nick") ?: "Usuario"

        // Reutilizamos el layout de amigos pero quitamos el FAB (porque no es nuestra lista)
        view.findViewById<View>(R.id.fab_add_friend).visibility = View.GONE
        
        // Cambiamos el título si existiera (opcional, el layout actual no tiene título arriba)
        
        val rvAmigos = view.findViewById<RecyclerView>(R.id.rv_amigos)
        val layoutEmpty = view.findViewById<View>(R.id.layout_empty)

        val listaAmigos = viewModel.getAmigosDeUsuario(idUsuario)
        
        val adapter = AmigosAdapter(listaAmigos) { amigo ->
            val bundle = Bundle().apply {
                putString("nick", amigo.nick)
                putInt("idNum", amigo.idNumerico)
            }
            // Podemos seguir navegando recursivamente a otros perfiles
            findNavController().navigate(R.id.perfilAmigoFragment, bundle)
        }

        rvAmigos.layoutManager = LinearLayoutManager(requireContext())
        rvAmigos.adapter = adapter

        layoutEmpty.visibility = if (listaAmigos.isEmpty()) View.VISIBLE else View.GONE
    }
}
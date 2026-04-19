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

        // Referencias de UI
        val rvAmigos = view.findViewById<RecyclerView>(R.id.rv_amigos)
        val fabAdd = view.findViewById<View>(R.id.fab_add_friend)
        val layoutEmpty = view.findViewById<View>(R.id.layout_empty)
        val tvEmptyDesc = view.findViewById<TextView>(R.id.tv_empty_desc)
        val tvTitle = view.findViewById<TextView>(R.id.tv_amigos_title)
        
        // Pulido de UI
        fabAdd.visibility = View.GONE
        tvTitle.text = "Amigos de $nick"
        tvEmptyDesc.text = "$nick aún no tiene amigos añadidos."

        // 1. Configurar el adaptador para navegar recursivamente a otros perfiles
        val adapter = AmigosAdapter(emptyList()) { amigo ->
            val bundle = Bundle().apply {
                putString("nick", amigo.nick)
                putInt("idNum", amigo.idNumerico)
            }
            // Esto permite que el usuario siga navegando de perfil en perfil
            findNavController().navigate(R.id.perfilAmigoFragment, bundle)
        }

        rvAmigos.layoutManager = LinearLayoutManager(requireContext())
        rvAmigos.adapter = adapter

        // 2. Observar los amigos del usuario seleccionado
        viewModel.amigosDeOtro.observe(viewLifecycleOwner) { listaAmigos ->
            adapter.updateData(listaAmigos)
            layoutEmpty.visibility = if (listaAmigos.isEmpty()) View.VISIBLE else View.GONE
        }

        // 3. Cargar los datos desde Firestore
        viewModel.cargarAmigosDeOtro(nick, idUsuario)
    }
}

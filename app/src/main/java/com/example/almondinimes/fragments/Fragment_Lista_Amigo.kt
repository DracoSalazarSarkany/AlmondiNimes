package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.adapters.MyObrasAdapter
import com.example.almondinimes.viewmodels.AmigosViewModel

class Fragment_Lista_Amigo : Fragment(R.layout.layout_lista_obras_usuario) {

    private val viewModel: AmigosViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar argumentos
        val idUsuario = arguments?.getInt("idUsuario") ?: 0
        val nick = arguments?.getString("nick") ?: "Usuario"
        val tipo = arguments?.getString("tipo") ?: "ANIME"

        // Referencias UI (Reutilizamos el layout de "Mis Listas")
        val rvObras = view.findViewById<RecyclerView>(R.id.rv_my_obras)
        val layoutEmpty = view.findViewById<View>(R.id.layout_empty)
        val tvEmptyDesc = view.findViewById<TextView>(R.id.tv_empty_desc)
        
        // Configurar texto vacío personalizado
        tvEmptyDesc.text = if (tipo == "ANIME") {
            "$nick aún no tiene animes en su lista."
        } else {
            "$nick aún no tiene mangas en su lista."
        }

        // 1. Configurar Adaptador (Lectura, sin clics para editar)
        val adapter = MyObrasAdapter(emptyList()) { /* No permitimos editar obras de otros */ }
        rvObras.layoutManager = LinearLayoutManager(requireContext())
        rvObras.adapter = adapter

        // 2. Observar las obras del amigo
        viewModel.obrasDeOtro.observe(viewLifecycleOwner) { lista ->
            adapter.updateData(lista)
            layoutEmpty.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }

        // 3. Cargar datos
        viewModel.cargarObrasDeOtro(nick, idUsuario, tipo)
    }
}

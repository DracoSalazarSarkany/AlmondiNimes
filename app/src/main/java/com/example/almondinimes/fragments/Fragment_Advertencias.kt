package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.adapters.NotificacionesAdapter
import com.example.almondinimes.models.Notificacion
import com.example.almondinimes.models.TipoNotificacion
import com.example.almondinimes.viewmodels.NotificacionesViewModel
import java.util.Date

class Fragment_Advertencias : Fragment() {

    private val viewModel: NotificacionesViewModel by viewModels()
    private lateinit var adapter: NotificacionesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_advertencias, container, false)
        
        val rv = view.findViewById<RecyclerView>(R.id.rv_advertencias)
        rv.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = NotificacionesAdapter(emptyList()) { notificacion ->
            viewModel.marcarComoLeida(notificacion)
        }
        rv.adapter = adapter

        viewModel.notificaciones.observe(viewLifecycleOwner) { lista ->
            adapter.updateList(lista.toList())
        }

        return view
    }
}

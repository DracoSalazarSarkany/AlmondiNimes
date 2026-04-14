package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.adapters.ObrasBusquedaAdapter
import com.example.almondinimes.network.RetrofitClient
import com.example.almondinimes.utils.EndlessScrollListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Fragment_Anime : Fragment(R.layout.fragment_anime) {

    private lateinit var adapter: ObrasBusquedaAdapter
    private lateinit var scrollListener: EndlessScrollListener
    private val service = RetrofitClient.service
    
    private var currentPage = 1
    private var searchJob: Job? = null
    private var isSearching = false
    private var currentQuery = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<EditText>(R.id.et_search_anime)
        val rvAnime = view.findViewById<RecyclerView>(R.id.rv_anime)

        val layoutManager = LinearLayoutManager(requireContext())
        rvAnime.layoutManager = layoutManager

        adapter = ObrasBusquedaAdapter(emptyList()) { anime ->
            NavegacionUtil.irADetalle(findNavController(), anime, "ANIME")
        }
        rvAnime.adapter = adapter

        // Uso del ScrollListener centralizado
        scrollListener = EndlessScrollListener(layoutManager) {
            cargarMasPaginas()
        }
        rvAnime.addOnScrollListener(scrollListener)

        etSearch.addTextChangedListener { text ->
            val query = text.toString().trim()
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                currentPage = 1
                if (query.length >= 3) {
                    delay(500)
                    isSearching = true
                    currentQuery = query
                    ejecutarBusqueda(query)
                } else if (query.isEmpty()) {
                    isSearching = false
                    cargarDatosIniciales()
                }
            }
        }
        
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = service.getTopAnime(currentPage)
                adapter.updateData(response.data)
            } catch (e: Exception) {
                mostrarError("Error al cargar Top Anime")
            } finally {
                scrollListener.setLoaded()
            }
        }
    }

    private fun ejecutarBusqueda(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = service.searchAnime(query)
                adapter.updateData(response.data)
            } catch (e: Exception) {
                // Manejo silencioso
            } finally {
                scrollListener.setLoaded()
            }
        }
    }

    private fun cargarMasPaginas() {
        viewLifecycleOwner.lifecycleScope.launch {
            currentPage++
            try {
                val response = if (isSearching) {
                    service.searchAnime(currentQuery) // Jikan v4 pagination can be added here
                } else {
                    service.getTopAnime(currentPage)
                }
                adapter.addMoreData(response.data)
            } catch (e: Exception) {
                currentPage--
            } finally {
                scrollListener.setLoaded()
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        if (isAdded) Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }
}

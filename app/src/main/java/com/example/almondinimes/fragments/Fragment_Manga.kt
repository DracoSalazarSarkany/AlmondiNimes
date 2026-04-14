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

class Fragment_Manga : Fragment(R.layout.fragment_manga) {

    private lateinit var adapter: ObrasBusquedaAdapter
    private lateinit var scrollListener: EndlessScrollListener
    private val service = RetrofitClient.service
    
    private var currentPage = 1
    private var isSearching = false
    private var currentQuery = ""
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<EditText>(R.id.et_search_manga)
        val rvManga = view.findViewById<RecyclerView>(R.id.rv_manga)

        val layoutManager = LinearLayoutManager(requireContext())
        rvManga.layoutManager = layoutManager

        adapter = ObrasBusquedaAdapter(emptyList()) { manga ->
            NavegacionUtil.irADetalle(findNavController(), manga, "MANGA")
        }
        rvManga.adapter = adapter

        scrollListener = EndlessScrollListener(layoutManager) {
            cargarMasPaginas()
        }
        rvManga.addOnScrollListener(scrollListener)

        etSearch.addTextChangedListener { text ->
            val query = text.toString().trim()
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                currentPage = 1
                if (query.length >= 3) {
                    delay(500)
                    isSearching = true
                    currentQuery = query
                    buscarManga(query)
                } else if (query.isEmpty()) {
                    isSearching = false
                    cargarTopManga()
                }
            }
        }

        cargarTopManga()
    }

    private fun cargarTopManga() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = service.getTopManga(currentPage)
                adapter.updateData(response.data)
            } catch (e: Exception) {
                mostrarError("Error al cargar Top Manga")
            } finally {
                scrollListener.setLoaded()
            }
        }
    }

    private fun buscarManga(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = service.searchManga(query, currentPage)
                adapter.updateData(response.data)
            } catch (e: Exception) {
                // Error handling
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
                    service.searchManga(currentQuery, currentPage)
                } else {
                    service.getTopManga(currentPage)
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

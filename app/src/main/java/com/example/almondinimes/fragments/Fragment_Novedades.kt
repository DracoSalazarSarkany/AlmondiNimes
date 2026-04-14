package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.adapters.NovedadesAdapter
import com.example.almondinimes.network.RetrofitClient
import com.example.almondinimes.utils.EndlessScrollListener
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class Fragment_Novedades : Fragment(R.layout.fragment_novedades) {

    private lateinit var adapter: NovedadesAdapter
    private lateinit var scrollListener: EndlessScrollListener
    private val service = RetrofitClient.service
    
    private var currentPage = 1
    private var currentTab = 0 // 0: Anime, 1: Manga

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_novedades)
        val rv = view.findViewById<RecyclerView>(R.id.rv_novedades)

        val layoutManager = LinearLayoutManager(requireContext())
        rv.layoutManager = layoutManager

        // Usamos el nuevo NovedadesAdapter
        adapter = NovedadesAdapter(emptyList()) { obra ->
            val tipo = if (currentTab == 0) "ANIME" else "MANGA"
            NavegacionUtil.irADetalle(findNavController(), obra, tipo)
        }
        rv.adapter = adapter

        scrollListener = EndlessScrollListener(layoutManager) {
            cargarMasPaginas()
        }
        rv.addOnScrollListener(scrollListener)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                currentPage = 1
                adapter.updateData(emptyList())
                cargarNovedades()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        cargarNovedades()
    }

    private fun cargarNovedades() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = if (currentTab == 0) {
                    service.getCurrentSeason(currentPage)
                } else {
                    service.getTopMangaByFilter("publishing", currentPage)
                }
                adapter.updateData(response.data)
            } catch (e: Exception) {
                mostrarError("Error al cargar novedades")
            } finally {
                scrollListener.setLoaded()
            }
        }
    }

    private fun cargarMasPaginas() {
        viewLifecycleOwner.lifecycleScope.launch {
            currentPage++
            try {
                val response = if (currentTab == 0) {
                    service.getCurrentSeason(currentPage)
                } else {
                    service.getTopMangaByFilter("publishing", currentPage)
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

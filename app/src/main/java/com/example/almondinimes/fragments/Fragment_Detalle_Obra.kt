package com.example.almondinimes.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.almondinimes.R
import com.example.almondinimes.models.ObraGuardada
import com.example.almondinimes.network.RetrofitClient
import com.example.almondinimes.viewmodels.ObrasViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class Fragment_Detalle_Obra : Fragment(R.layout.fragment_detalle_obra) {

    private val obrasViewModel: ObrasViewModel by activityViewModels()

    private var currentMalId: Int = 0
    private var currentTitle: String = ""
    private var currentImageUrl: String = ""
    private var currentType: String = "ANIME"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivCover = view.findViewById<ImageView>(R.id.iv_details_cover)
        val tvTitle = view.findViewById<TextView>(R.id.tv_details_title)
        val tvSynopsis = view.findViewById<TextView>(R.id.tv_details_synopsis)
        val tvScore = view.findViewById<TextView>(R.id.tv_details_score)
        val tvStatus = view.findViewById<TextView>(R.id.tv_details_status)
        val tvCount = view.findViewById<TextView>(R.id.tv_details_count)
        val cgGenres = view.findViewById<ChipGroup>(R.id.cg_details_genres)
        val btnAdd = view.findViewById<Button>(R.id.btn_details_add_to_list)

        currentMalId = arguments?.getInt("mal_id") ?: 0
        currentTitle = arguments?.getString("titulo") ?: "Sin título"
        currentImageUrl = arguments?.getString("url_imagen") ?: ""
        currentType = arguments?.getString("tipo") ?: "ANIME"
        val initialSynopsis = arguments?.getString("sinopsis") ?: "Cargando..."
        val initialScore = arguments?.getDouble("score") ?: 0.0
        val generos = arguments?.getStringArrayList("generos")

        tvTitle.text = currentTitle
        tvSynopsis.text = if (initialSynopsis == "Cargando...") "Cargando información oficial..." else initialSynopsis
        tvScore.text = "⭐ $initialScore"
        Glide.with(this).load(currentImageUrl).into(ivCover)

        cgGenres.removeAllViews()
        generos?.forEach { nombreGenero ->
            if (nombreGenero != "ANIME" && nombreGenero != "MANGA") {
                val chip = Chip(requireContext())
                chip.text = nombreGenero
                chip.setChipBackgroundColorResource(R.color.black)
                chip.setTextColor(resources.getColor(android.R.color.white, null))
                cgGenres.addView(chip)
            }
        }

        if (currentMalId != 0) {
            fetchFullData(currentMalId, currentType, tvTitle, tvSynopsis, tvScore, cgGenres, ivCover, tvStatus, tvCount)
        }

        btnAdd.setOnClickListener {
            val nuevaObra = ObraGuardada(
                malId = currentMalId,
                title = currentTitle,
                imageUrl = currentImageUrl,
                type = currentType
            )
            obrasViewModel.añadirObra(nuevaObra)
            Toast.makeText(requireContext(), "$currentTitle añadido a tu lista", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFullData(id: Int, tipo: String, tvTit: TextView, tvSyn: TextView, tvSco: TextView, cg: ChipGroup, iv: ImageView, tvStatus: TextView, tvCount: TextView) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = if (tipo == "ANIME") {
                    RetrofitClient.service.getAnimeFull(id)
                } else {
                    RetrofitClient.service.getMangaFull(id)
                }

                val obra = response.data
                currentTitle = obra.title
                currentImageUrl = obra.images.jpg.url

                tvTit.text = obra.title
                tvSyn.text = obra.synopsis ?: "Sin sinopsis disponible."
                tvSco.text = "⭐ ${obra.score ?: 0.0}"
                
                val status = obra.status ?: "Unknown"
                tvStatus.text = status
                
                if (status.contains("Currently Airing", ignoreCase = true) || 
                    status.contains("Publishing", ignoreCase = true)) {
                    tvStatus.setTextColor(android.graphics.Color.GREEN)
                } else if (status.contains("Finished", ignoreCase = true)) {
                    tvStatus.setTextColor(android.graphics.Color.RED)
                } else {
                    tvStatus.setTextColor(android.graphics.Color.GRAY)
                }

                if (tipo == "ANIME") {
                    val eps = obra.episodes ?: "?"
                    tvCount.text = "Episodios: $eps"
                } else {
                    val caps = obra.chapters ?: "?"
                    val vols = obra.volumes ?: "?"
                    tvCount.text = "Cap: $caps | Vol: $vols"
                }

                Glide.with(this@Fragment_Detalle_Obra).load(currentImageUrl).into(iv)

                cg.removeAllViews()
                obra.genres?.forEach { genre ->
                    val chip = Chip(requireContext())
                    chip.text = genre.name
                    chip.setChipBackgroundColorResource(R.color.black)
                    chip.setTextColor(resources.getColor(android.R.color.white, null))
                    cg.addView(chip)
                }
            } catch (e: Exception) {
                Log.e("DetalleObra", "Error al cargar datos: ${e.message}")
            }
        }
    }
}

package com.example.almondinimes.fragments

import android.os.Bundle
import androidx.navigation.NavController
import com.example.almondinimes.R
import com.example.almondinimes.models.Anime

object NavegacionUtil {
    fun irADetalle(navController: NavController, anime: Anime, tipo: String) {
        val bundle = Bundle().apply {
            putInt("mal_id", anime.mal_id)
            putString("titulo", anime.title)
            putString("sinopsis", anime.synopsis)
            putDouble("score", anime.score ?: 0.0)
            putString("url_imagen", anime.images.jpg.url)
            putString("tipo", tipo)
            putStringArrayList("generos", ArrayList(anime.genres?.map { it.name } ?: emptyList()))
        }
        navController.navigate(R.id.detalleObraFragment, bundle)
    }
}

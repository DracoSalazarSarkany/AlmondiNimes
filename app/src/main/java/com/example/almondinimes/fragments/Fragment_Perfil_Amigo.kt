package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.almondinimes.R
import com.example.almondinimes.viewmodels.AmigosViewModel

class Fragment_Perfil_Amigo : Fragment(R.layout.fragment_perfil_amigo) {

    // Instancia compartida del ViewModel vinculada a la Activity
    private val viewModel: AmigosViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Recuperar los datos enviados desde la lista de amigos
        val nick = arguments?.getString("nick") ?: "Usuario"
        val idNum = arguments?.getInt("idNum") ?: 0

        // 2. Referencias de la UI
        val tvHeader = view.findViewById<TextView>(R.id.tv_friend_profile_header)
        val ivPhoto = view.findViewById<ImageView>(R.id.iv_friend_profile_photo)

        val cvAnime = view.findViewById<CardView>(R.id.cv_friend_anime)
        val cvManga = view.findViewById<CardView>(R.id.cv_friend_manga)
        val cvFriends = view.findViewById<CardView>(R.id.cv_friend_friends)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete_friend)

        // 3. Setear los datos (Usando el formato Nick#0000X)
        val fullId = String.format("%s#%05d", nick, idNum)
        tvHeader.text = "Perfil de $fullId"

        // 4. Configurar clics en las secciones para navegar
        cvAnime.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("idUsuario", idNum)
                putString("nick", nick)
                putString("tipo", "ANIME")
            }
            findNavController().navigate(R.id.action_perfilAmigo_to_listaAmigo, bundle)
        }

        cvManga.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("idUsuario", idNum)
                putString("nick", nick)
                putString("tipo", "MANGA")
            }
            findNavController().navigate(R.id.action_perfilAmigo_to_listaAmigo, bundle)
        }

        cvFriends.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("idUsuario", idNum)
                putString("nick", nick)
            }
            findNavController().navigate(R.id.action_perfilAmigo_to_amigosDeAmigo, bundle)
        }

        // 5. Botón Borrar Amigo conectado al ViewModel
        btnDelete.setOnClickListener {
            // Ejecutamos la lógica de borrado en el ViewModel compartido
            viewModel.borrarAmigo(idNum)

            Toast.makeText(requireContext(), "$nick eliminado de tus amigos", Toast.LENGTH_SHORT).show()

            // Volvemos atrás. Al llegar a Fragment_Amigos, la lista ya estará actualizada
            findNavController().popBackStack()
        }
    }
}
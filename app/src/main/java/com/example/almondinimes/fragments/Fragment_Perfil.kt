package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.almondinimes.R
import androidx.fragment.app.activityViewModels
import com.example.almondinimes.viewmodels.ObrasViewModel

class Fragment_Perfil : Fragment(R.layout.fragment_perfil) {

    private val obrasViewModel: ObrasViewModel by activityViewModels()

    // Referencias de UI
    private lateinit var tvUserId: TextView
    private lateinit var tvNombre: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvFechaNacimiento: TextView
    private lateinit var ivFotoPerfil: ImageView

    // Referencias de Estadísticas
    private lateinit var tvStatCompleted: TextView
    private lateinit var tvStatWatching: TextView
    private lateinit var tvStatPending: TextView
    private lateinit var tvStatDropped: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar vistas
        tvUserId = view.findViewById(R.id.tv_profile_user_id)
        tvNombre = view.findViewById(R.id.tv_profile_name)
        tvEmail = view.findViewById(R.id.tv_profile_email)
        tvFechaNacimiento = view.findViewById(R.id.tv_profile_birth)
        ivFotoPerfil = view.findViewById(R.id.iv_profile_photo)

        tvStatCompleted = view.findViewById(R.id.tv_stat_completed_count)
        tvStatWatching = view.findViewById(R.id.tv_stat_watching_count)
        tvStatPending = view.findViewById(R.id.tv_stat_pending_count)
        tvStatDropped = view.findViewById(R.id.tv_stat_dropped_count)

        val btnEditar = view.findViewById<Button>(R.id.btn_edit_profile)
        val btnEliminar = view.findViewById<Button>(R.id.btn_delete_account)

        // 2. Cargar datos básicos de usuario
        cargarDatosDeUsuario()

        // 3. Observar listas para actualizar estadísticas en tiempo real
        obrasViewModel.listaAnimes.observe(viewLifecycleOwner) { actualizarEstadisticas() }
        obrasViewModel.listaMangas.observe(viewLifecycleOwner) { actualizarEstadisticas() }

        // 4. Listeners
        btnEditar.setOnClickListener {
            findNavController().navigate(R.id.editarPerfilFragment)
        }

        btnEliminar.setOnClickListener {
            // Placeholder para lógica de borrado
        }
    }

    private fun actualizarEstadisticas() {
        val animes = obrasViewModel.listaAnimes.value.orEmpty()
        val mangas = obrasViewModel.listaMangas.value.orEmpty()
        val todasLasObras = animes + mangas

        val completados = todasLasObras.count { it.status == "Completado" }
        val viendo = todasLasObras.count { it.status == "Viendo" }
        val pendientes = todasLasObras.count { it.status == "Pendiente" }
        val dropeados = todasLasObras.count { it.status == "Dropeado" }

        tvStatCompleted.text = completados.toString()
        tvStatWatching.text = viendo.toString()
        tvStatPending.text = pendientes.toString()
        tvStatDropped.text = dropeados.toString()
    }

    /**
     * FUNCIÓN CLAVE: Ahora usa datos hardcodeados,
     * pero está lista para recibir el repositorio de datos real.
     */
    private fun cargarDatosDeUsuario() {
        // Estos datos vendrán del futuro Repositorio/DB
        val nickUsuario = "Gemini"
        val idNumerico = 42 // Este será el valor autoincremental de la DB

        val emailEjemplo = "usuario@almondinimes.com"
        val fechaEjemplo = "15/05/1998"

        // Formateamos el ID para que siempre tenga 5 cifras: Nick#00042
        val idFormateado = String.format("%s#%05d", nickUsuario, idNumerico)

        // Asignación a la UI
        tvUserId.text = idFormateado
        tvNombre.text = nickUsuario
        tvEmail.text = emailEjemplo
        tvFechaNacimiento.text = fechaEjemplo
    }

}
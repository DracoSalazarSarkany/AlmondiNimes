package com.example.almondinimes.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.almondinimes.R
import androidx.fragment.app.activityViewModels
import com.example.almondinimes.data.AuthManager
import com.example.almondinimes.viewmodels.ObrasViewModel

class Fragment_Perfil : Fragment(R.layout.fragment_perfil) {

    private val obrasViewModel: ObrasViewModel by activityViewModels()
    private val authManager = AuthManager()

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
        // Observamos ambas listas y actualizamos cuando cualquiera cambie
        obrasViewModel.listaAnimes.observe(viewLifecycleOwner) { 
            actualizarEstadisticas() 
        }
        obrasViewModel.listaMangas.observe(viewLifecycleOwner) { 
            actualizarEstadisticas() 
        }

        // 4. Listeners
        btnEditar.setOnClickListener {
            findNavController().navigate(R.id.editarPerfilFragment)
        }

        btnEliminar.setOnClickListener {
            mostrarDialogoConfirmacionBorrado()
        }
    }

    private fun mostrarDialogoConfirmacionBorrado() {
        AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
            .setTitle("Eliminar cuenta")
            .setMessage("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer y perderás todas tus listas.")
            .setPositiveButton("Eliminar") { _, _ ->
                authManager.deleteAccount { success, error ->
                    if (success) {
                        Toast.makeText(requireContext(), "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show()
                        // Navegar de vuelta al login
                        findNavController().navigate(R.id.loginFragment)
                    } else {
                        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
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
     * FUNCIÓN CLAVE: Ahora carga datos reales desde Firestore en tiempo real.
     */
    private fun cargarDatosDeUsuario() {
        val uid = authManager.getCurrentUserUid()
        if (uid != null) {
            authManager.listenToUserData(uid) { usuario ->
                if (usuario != null) {
                    // Asignación a la UI con datos reales (fullId se calcula solo)
                    tvUserId.text = usuario.fullId
                    tvNombre.text = usuario.nick
                    tvEmail.text = usuario.email
                    tvFechaNacimiento.text = usuario.birthDate
                }
            }
        }
    }

}
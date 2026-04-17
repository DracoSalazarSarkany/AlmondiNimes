package com.example.almondinimes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.almondinimes.models.ObraGuardada

class ObrasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _listaAnimes = MutableLiveData<List<ObraGuardada>>(emptyList())
    val listaAnimes: LiveData<List<ObraGuardada>> = _listaAnimes

    private val _listaMangas = MutableLiveData<List<ObraGuardada>>(emptyList())
    val listaMangas: LiveData<List<ObraGuardada>> = _listaMangas

    init {
        escucharObras()
    }

    /**
     * Escucha los cambios en la subcolección de obras del usuario en tiempo real.
     */
    private fun escucharObras() {
        val uid = auth.currentUser?.uid ?: return
        
        db.collection("users").document(uid).collection("obras_usuario")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                
                val todasLasObras = snapshot.toObjects(ObraGuardada::class.java)
                
                // Filtramos por tipo y actualizamos los LiveData
                _listaAnimes.value = todasLasObras.filter { it.type == "ANIME" }
                _listaMangas.value = todasLasObras.filter { it.type == "MANGA" }
            }
    }

    /**
     * Añade o actualiza una obra en Firestore.
     */
    fun añadirObra(obra: ObraGuardada) {
        val uid = auth.currentUser?.uid ?: return
        if (obra.malId == 0) return

        db.collection("users").document(uid)
            .collection("obras_usuario")
            .document(obra.malId.toString())
            .set(obra)
    }

    /**
     * Elimina una obra de Firestore.
     */
    fun eliminarObra(obra: ObraGuardada) {
        val uid = auth.currentUser?.uid ?: return
        
        db.collection("users").document(uid)
            .collection("obras_usuario")
            .document(obra.malId.toString())
            .delete()
    }

    /**
     * Alias de añadirObra para claridad en la edición.
     */
    fun actualizarObra(obraActualizada: ObraGuardada) {
        añadirObra(obraActualizada)
    }
}

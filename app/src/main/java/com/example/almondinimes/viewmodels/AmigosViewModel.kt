package com.example.almondinimes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.almondinimes.models.Usuario
import com.example.almondinimes.models.ObraGuardada
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AmigosViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _listaAmigos = MutableLiveData<List<Usuario>>(emptyList())
    val listaAmigos: LiveData<List<Usuario>> get() = _listaAmigos

    private val _resultadosBusqueda = MutableLiveData<List<Usuario>>(emptyList())
    val resultadosBusqueda: LiveData<List<Usuario>> get() = _resultadosBusqueda

    init {
        escucharAmigos()
    }

    private fun escucharAmigos() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("amigos")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                _listaAmigos.value = snapshot.toObjects(Usuario::class.java)
            }
    }

    /**
     * Busca un usuario por su tag completo (ej: Nick#00001)
     */
    fun buscarUsuarioPorTag(tag: String) {
        if (!tag.contains("#")) {
            _resultadosBusqueda.value = emptyList()
            return
        }

        val partes = tag.split("#")
        val nickBusqueda = partes[0]
        val idNumBusqueda = partes[1].toIntOrNull() ?: 0

        db.collection("users")
            .whereEqualTo("nick", nickBusqueda)
            .whereEqualTo("idNumerico", idNumBusqueda)
            .get()
            .addOnSuccessListener { snapshot ->
                val usuarios = snapshot.toObjects(Usuario::class.java)
                _resultadosBusqueda.value = usuarios
            }
            .addOnFailureListener {
                _resultadosBusqueda.value = emptyList()
            }
    }

    fun añadirAmigos(nuevos: Set<Usuario>) {
        val uid = auth.currentUser?.uid ?: return
        val batch = db.batch()

        nuevos.forEach { amigo ->
            val ref = db.collection("users").document(uid).collection("amigos").document(amigo.uid)
            batch.set(ref, amigo)
        }

        batch.commit()
    }

    /**
     * Obtiene las obras (Anime/Manga) de otro usuario.
     */
    private val _obrasDeOtro = MutableLiveData<List<ObraGuardada>>(emptyList())
    val obrasDeOtro: LiveData<List<ObraGuardada>> get() = _obrasDeOtro

    fun cargarObrasDeOtro(nick: String, idNum: Int, tipo: String) {
        // Buscamos al usuario para tener su UID real
        db.collection("users")
            .whereEqualTo("nick", nick)
            .whereEqualTo("idNumerico", idNum)
            .get()
            .addOnSuccessListener { snapshot ->
                val uid = snapshot.documents.firstOrNull()?.id
                if (uid != null) {
                    // Filtramos por tipo (ANIME/MANGA) en su subcolección
                    db.collection("users").document(uid).collection("obras_usuario")
                        .whereEqualTo("tipo", tipo)
                        .get()
                        .addOnSuccessListener { obrasSnapshot ->
                            _obrasDeOtro.value = obrasSnapshot.toObjects(ObraGuardada::class.java)
                        }
                }
            }
    }

    /**
     * Obtiene los amigos de otro usuario para visualizarlos.
     */
    private val _amigosDeOtro = MutableLiveData<List<Usuario>>(emptyList())
    val amigosDeOtro: LiveData<List<Usuario>> get() = _amigosDeOtro

    fun cargarAmigosDeOtro(nick: String, idNum: Int) {
        // 1. Buscamos al usuario por su tag para obtener su UID
        db.collection("users")
            .whereEqualTo("nick", nick)
            .whereEqualTo("idNumerico", idNum)
            .get()
            .addOnSuccessListener { snapshot ->
                val uid = snapshot.documents.firstOrNull()?.id
                if (uid != null) {
                    // 2. Una vez tenemos el UID, traemos su subcolección de amigos
                    db.collection("users").document(uid).collection("amigos").get()
                        .addOnSuccessListener { amigosSnapshot ->
                            _amigosDeOtro.value = amigosSnapshot.toObjects(Usuario::class.java)
                        }
                }
            }
    }

    fun borrarAmigo(idParaBorrar: Int) {
        // Implementaremos el borrado por ID numérico buscando el UID primero
        val uidActual = auth.currentUser?.uid ?: return
        db.collection("users").document(uidActual).collection("amigos")
            .whereEqualTo("idNumerico", idParaBorrar)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.forEach { doc -> doc.reference.delete() }
            }
    }
}

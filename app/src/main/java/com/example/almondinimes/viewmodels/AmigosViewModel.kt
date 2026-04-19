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
     * Busca usuarios de forma dinámica por Nick (empieza por) o por Tag completo (Nick#0000)
     */
    fun buscarUsuarios(query: String) {
        val currentUid = auth.currentUser?.uid
        if (query.isEmpty()) {
            _resultadosBusqueda.value = emptyList()
            return
        }

        val collection = db.collection("users")

        if (query.startsWith("#")) {
            // Caso 1: Búsqueda SOLO por ID (Ej: #00001)
            val idStr = query.removePrefix("#")
            val idNumBusqueda = idStr.toIntOrNull() ?: -1
            
            if (idNumBusqueda == -1) {
                _resultadosBusqueda.value = emptyList()
                return
            }

            collection.whereEqualTo("idNumerico", idNumBusqueda)
                .get()
                .addOnSuccessListener { snapshot ->
                    _resultadosBusqueda.value = snapshot.toObjects(Usuario::class.java).filter { it.uid != currentUid }
                }

        } else if (query.contains("#")) {
            // Caso 2: Búsqueda por TAG completo (Ej: Nick#00001)
            val partes = query.split("#")
            val nickBusqueda = partes[0]
            val idNumBusqueda = partes[1].toIntOrNull() ?: -1

            collection
                .whereEqualTo("nick", nickBusqueda)
                .whereEqualTo("idNumerico", idNumBusqueda)
                .get()
                .addOnSuccessListener { snapshot ->
                    _resultadosBusqueda.value = snapshot.toObjects(Usuario::class.java).filter { it.uid != currentUid }
                }
        } else {
            // Caso 3: Búsqueda por NICK (prefijo)
            if (query.length < 3) return // Evitar búsquedas demasiado amplias

            collection
                .orderBy("nick")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .limit(10)
                .get()
                .addOnSuccessListener { snapshot ->
                    _resultadosBusqueda.value = snapshot.toObjects(Usuario::class.java).filter { it.uid != currentUid }
                }
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
                    // Filtramos por tipo (ANIME/MANGA) en su subcolección usando el campo 'type'
                    db.collection("users").document(uid).collection("obras_usuario")
                        .whereEqualTo("type", tipo)
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

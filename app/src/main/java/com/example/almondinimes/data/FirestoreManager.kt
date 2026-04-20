package com.example.almondinimes.data

import com.example.almondinimes.models.Usuario
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Crea un perfil de usuario con ID autoincremental usando una transacción.
     */
    fun createUserProfile(uid: String, email: String, nick: String, age: Int, birthDate: String, onResult: (Boolean, String?) -> Unit) {
        val counterRef = db.collection("metadata").document("user_counter")
        val userRef = db.collection("users").document(uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(counterRef)
            val currentCount = snapshot.getLong("count") ?: 0L
            val nextCount = currentCount + 1
            
            transaction.set(counterRef, mapOf("count" to nextCount))
            
            val nuevoUsuario = Usuario(
                uid = uid,
                nick = nick,
                email = email,
                idNumerico = nextCount.toInt(),
                age = age,
                birthDate = birthDate
            )
            transaction.set(userRef, nuevoUsuario)
            null
        }.addOnSuccessListener {
            onResult(true, null)
        }.addOnFailureListener { e ->
            onResult(false, e.message)
        }
    }

    /**
     * Obtiene los datos del usuario (una sola vez) desde Firestore.
     */
    fun getUserData(uid: String, onResult: (Usuario?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.toObject(Usuario::class.java))
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    /**
     * Escucha los cambios del usuario en tiempo real.
     */
    fun listenToUserData(uid: String, onResult: (Usuario?) -> Unit) {
        db.collection("users").document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onResult(null)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    onResult(snapshot.toObject(Usuario::class.java))
                }
            }
    }

    /**
     * Actualiza los datos del perfil.
     */
    fun updateProfile(uid: String, nick: String, age: Int, birthDate: String, onResult: (Boolean, String?) -> Unit) {
        val updates = mutableMapOf<String, Any>(
            "nick" to nick,
            "age" to age,
            "birthDate" to birthDate,
            "fullId" to FieldValue.delete()
        )
        db.collection("users").document(uid).update(updates)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    /**
     * Borra el perfil del usuario y todos sus datos (obras y amigos) de forma recursiva.
     */
    fun deleteUserData(uid: String, onResult: (Boolean, String?) -> Unit) {
        val userRef = db.collection("users").document(uid)
        
        // 1. Obtener las subcolecciones (necesitamos borrar documento a documento)
        val obrasRef = userRef.collection("obras_usuario")
        val amigosRef = userRef.collection("amigos")

        obrasRef.get().addOnSuccessListener { obrasSnapshot ->
            amigosRef.get().addOnSuccessListener { amigosSnapshot ->
                
                val batch = db.batch()

                // Añadir borrado de obras al batch
                for (doc in obrasSnapshot.documents) {
                    batch.delete(doc.reference)
                }

                // Añadir borrado de amigos al batch
                for (doc in amigosSnapshot.documents) {
                    batch.delete(doc.reference)
                }

                // Finalmente borrar el documento del perfil
                batch.delete(userRef)

                // Ejecutar todo el borrado de golpe
                batch.commit()
                    .addOnSuccessListener { onResult(true, null) }
                    .addOnFailureListener { e -> onResult(false, e.message) }

            }.addOnFailureListener { e -> onResult(false, "Error al acceder a amigos: ${e.message}") }
        }.addOnFailureListener { e -> onResult(false, "Error al acceder a obras: ${e.message}") }
    }
}

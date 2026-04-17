package com.example.almondinimes.data

import com.example.almondinimes.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class AuthManager {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Registra un nuevo usuario en Firebase Auth y guarda sus datos en Firestore con un ID autoincremental.
     */
    fun register(email: String, pass: String, nick: String, age: Int, birthDate: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: ""
                    
                    val counterRef = db.collection("metadata").document("user_counter")
                    val userRef = db.collection("users").document(uid)

                    // Usamos una transacción para asegurar que el ID sea único y autoincremental
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(counterRef)
                        val currentCount = snapshot.getLong("count") ?: 0L
                        val nextCount = currentCount + 1
                        
                        // 1. Actualizar el contador global en la DB
                        transaction.set(counterRef, mapOf("count" to nextCount))
                        
                        // 2. Crear el objeto Usuario con el nuevo ID
                        val nuevoUsuario = Usuario(
                            uid = uid,
                            nick = nick,
                            email = email,
                            idNumerico = nextCount.toInt(),
                            age = age,
                            birthDate = birthDate
                        )
                        
                        // 3. Guardar el documento del usuario
                        transaction.set(userRef, nuevoUsuario)
                        
                        // Retornamos null o cualquier cosa, lo importante es que no lance excepción
                        null
                    }.addOnSuccessListener {
                        onResult(true, null)
                    }.addOnFailureListener { e ->
                        onResult(false, "Error al crear perfil: ${e.message}")
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    /**
     * Inicia sesión con email y contraseña.
     */
    fun login(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    /**
     * Cierra la sesión activa.
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Obtiene el UID del usuario actual si está logueado.
     */
    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Obtiene los datos del usuario desde Firestore.
     */
    fun getUserData(uid: String, onResult: (Usuario?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val usuario = document.toObject(Usuario::class.java)
                onResult(usuario)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}

package com.example.almondinimes.data

import com.example.almondinimes.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class AuthManager {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Registra un nuevo usuario en Firebase Auth y guarda sus datos en Firestore.
     */
    fun register(email: String, pass: String, nick: String, age: Int, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: ""
                    // Generamos un ID numérico aleatorio (como el que tenías en el modelo)
                    val idNumerico = Random.nextInt(1, 99999)
                    
                    val nuevoUsuario = Usuario(
                        uid = uid,
                        nick = nick,
                        email = email,
                        idNumerico = idNumerico,
                        age = age
                    )

                    // Guardamos en Firestore
                    db.collection("users").document(uid).set(nuevoUsuario)
                        .addOnSuccessListener {
                            onResult(true, null)
                        }
                        .addOnFailureListener { e ->
                            onResult(false, e.message)
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

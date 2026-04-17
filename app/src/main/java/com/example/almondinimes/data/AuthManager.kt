package com.example.almondinimes.data

import com.example.almondinimes.models.Usuario
import com.google.firebase.auth.FirebaseAuth

class AuthManager {

    private val auth = FirebaseAuth.getInstance()
    private val firestoreManager = FirestoreManager()

    /**
     * Registra un nuevo usuario en Firebase Auth y crea su perfil en Firestore.
     */
    fun register(email: String, pass: String, nick: String, age: Int, birthDate: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: ""
                    firestoreManager.createUserProfile(uid, email, nick, age, birthDate, onResult)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    /**
     * Escucha los cambios del usuario en tiempo real (Delegado a FirestoreManager).
     */
    fun listenToUserData(uid: String, onResult: (Usuario?) -> Unit) {
        firestoreManager.listenToUserData(uid, onResult)
    }

    /**
     * Actualiza los datos del perfil (Delegado a FirestoreManager).
     */
    fun updateProfile(nick: String, age: Int, birthDate: String, onResult: (Boolean, String?) -> Unit) {
        val uid = getCurrentUserUid()
        if (uid != null) {
            firestoreManager.updateProfile(uid, nick, age, birthDate, onResult)
        } else {
            onResult(false, "No hay sesión activa")
        }
    }

    /**
     * Obtiene los datos del usuario una vez (Delegado a FirestoreManager).
     */
    fun getUserData(uid: String, onResult: (Usuario?) -> Unit) {
        firestoreManager.getUserData(uid, onResult)
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
     * Obtiene el UID del usuario actual.
     */
    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Elimina la cuenta de usuario y sus datos.
     */
    fun deleteAccount(onResult: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            firestoreManager.deleteUserData(uid) { success, error ->
                if (success) {
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) onResult(true, null)
                        else onResult(false, task.exception?.message)
                    }
                } else {
                    onResult(false, error)
                }
            }
        } else {
            onResult(false, "No hay sesión activa")
        }
    }
}

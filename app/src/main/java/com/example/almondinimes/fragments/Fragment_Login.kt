package com.example.almondinimes.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.almondinimes.R
import com.example.almondinimes.activitys.MainActivity_Principal
import com.example.almondinimes.data.AuthManager

class Fragment_Login : Fragment(R.layout.fragment_login) {

    private val authManager = AuthManager()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los componentes del XML
        val etEmail = view.findViewById<EditText>(R.id.et_user) // Usamos el campo de user para el email
        val etPass = view.findViewById<EditText>(R.id.et_password)
        val cbRemember = view.findViewById<CheckBox>(R.id.cb_remember)
        val btnEntrar = view.findViewById<Button>(R.id.btn_login)
        val btnRegistrar = view.findViewById<TextView>(R.id.tv_register)

        // Inicializar SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        // Cargar datos si existen
        val savedEmail = sharedPref.getString("email", "")
        val isRemembered = sharedPref.getBoolean("remember", false)

        if (isRemembered && !savedEmail.isNullOrEmpty()) {
            etEmail.setText(savedEmail)
            cbRemember.isChecked = true
        }

        // Comprobar si ya hay una sesión iniciada en Firebase
        val currentUserUid = authManager.getCurrentUserUid()
        if (currentUserUid != null) {
            val intent = Intent(requireContext(), MainActivity_Principal::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Acción: Ir al fragmento de Registro
        btnRegistrar.setOnClickListener {
            // Usamos el ID de la acción que definimos en el nav_graph_login
            findNavController().navigate(R.id.action_login_to_registro)
        }

        // Acción: Entrar a la App Principal
        btnEntrar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                authManager.login(email, pass) { success, error ->
                    if (success) {
                        // Guardar en SharedPreferences si el CheckBox está marcado
                        val editor = sharedPref.edit()
                        if (cbRemember.isChecked) {
                            editor.putString("email", email)
                            editor.putBoolean("remember", true)
                        } else {
                            editor.clear()
                        }
                        editor.apply()

                        val intent = Intent(requireContext(), MainActivity_Principal::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${error ?: "Login fallido"}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
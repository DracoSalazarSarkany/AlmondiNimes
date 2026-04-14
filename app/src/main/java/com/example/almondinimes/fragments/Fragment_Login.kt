package com.example.almondinimes.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.almondinimes.R
import com.example.almondinimes.activitys.MainActivity_Principal

class Fragment_Login : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los componentes del XML
        val etUser = view.findViewById<EditText>(R.id.et_user)
        val etPass = view.findViewById<EditText>(R.id.et_password)
        val cbRemember = view.findViewById<CheckBox>(R.id.cb_remember)
        val btnEntrar = view.findViewById<Button>(R.id.btn_login)
        val btnRegistrar = view.findViewById<TextView>(R.id.tv_register)

        // Inicializar SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        // Cargar datos si existen
        val savedUser = sharedPref.getString("user", "")
        val savedPass = sharedPref.getString("pass", "")
        val isRemembered = sharedPref.getBoolean("remember", false)

        if (isRemembered && !savedUser.isNullOrEmpty() && !savedPass.isNullOrEmpty()) {
            val intent = Intent(requireContext(), MainActivity_Principal::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        if (isRemembered) {
            etUser.setText(savedUser)
            etPass.setText(savedPass)
            cbRemember.isChecked = true
        }

        // Acción: Ir al fragmento de Registro
        btnRegistrar.setOnClickListener {
            // Usamos el ID de la acción que definimos en el nav_graph_login
            findNavController().navigate(R.id.action_login_to_registro)
        }

        // Acción: Entrar a la App Principal
        btnEntrar.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                // Guardar en SharedPreferences si el CheckBox está marcado
                val editor = sharedPref.edit()
                if (cbRemember.isChecked) {
                    editor.putString("user", user)
                    editor.putString("pass", pass)
                    editor.putBoolean("remember", true)
                } else {
                    // Si no está marcado, limpiamos por seguridad
                    editor.clear()
                }
                editor.apply()

                // Simulación de login correcto
                val intent = Intent(requireContext(), MainActivity_Principal::class.java)
                startActivity(intent)

                // Cerramos la actividad de login para que no se pueda volver atrás con el botón del móvil
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
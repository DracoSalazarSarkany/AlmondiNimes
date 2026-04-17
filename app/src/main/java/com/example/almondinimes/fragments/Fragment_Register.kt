package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.almondinimes.R
import com.example.almondinimes.data.AuthManager
import java.util.*

class Fragment_Register : Fragment(R.layout.fragment_register) {

    private val authManager = AuthManager()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Referencias con IDs
        val etUser = view.findViewById<EditText>(R.id.et_reg_user)
        val etEmail = view.findViewById<EditText>(R.id.et_reg_email)
        val etPass = view.findViewById<EditText>(R.id.et_reg_password)
        val etPassRepeat = view.findViewById<EditText>(R.id.et_reg_repeat_password)

        val spDay = view.findViewById<Spinner>(R.id.spinner_day)
        val spMonth = view.findViewById<Spinner>(R.id.spinner_month)
        val spYear = view.findViewById<Spinner>(R.id.spinner_year)

        val btnAccept = view.findViewById<Button>(R.id.btn_accept_register)
        val btnBack = view.findViewById<Button>(R.id.btn_back)

        // 2. Configurar los Spinners
        setupDateSpinners(spDay, spMonth, spYear)

        // Botón Volver
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Botón Aceptar
        btnAccept.setOnClickListener {
            val user = etUser.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pass = etPass.text.toString().trim()
            val passRepeat = etPassRepeat.text.toString().trim()

            // Validaciones básicas
            if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != passRepeat) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Obtener fecha de los spinners y calcular edad
            val day = spDay.selectedItem.toString().toInt()
            val month = spMonth.selectedItemPosition // 0 a 11
            val year = spYear.selectedItem.toString().toInt()

            val age = calculateAge(day, month, year)
            val birthDate = String.format("%02d/%02d/%04d", day, month + 1, year)

            // Llamada a Firebase para registrar
            authManager.register(email, pass, user, age, birthDate) { success, error ->
                if (success) {
                    authManager.logout() // Cerramos la sesión automática tras el registro
                    Toast.makeText(requireContext(), "Registro exitoso. Ya puedes iniciar sesión.", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack() // Vuelve al Login
                } else {
                    Toast.makeText(requireContext(), "Error: ${error ?: "No se pudo registrar"}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupDateSpinners(spDay: Spinner, spMonth: Spinner, spYear: Spinner) {
        // Días (1-31)
        val days = (1..31).toList().map { it.toString() }
        spDay.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, days)

        // Meses (Nombres)
        val months = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
        spMonth.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)

        // Años (Desde hoy hacia atrás 100 años)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear downTo currentYear - 100).toList().map { it.toString() }
        spYear.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)
    }

    private fun calculateAge(day: Int, month: Int, year: Int): Int {
        val today = Calendar.getInstance()
        val birthDate = Calendar.getInstance()

        birthDate.set(year, month, day)

        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        // Si aún no ha llegado su cumpleaños este año, restamos uno
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }
}
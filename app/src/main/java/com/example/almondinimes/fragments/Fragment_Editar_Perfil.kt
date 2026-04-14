package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.almondinimes.R

class Fragment_Editar_Perfil : Fragment(R.layout.fragment_editar_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Referencias de UI
        val etName = view.findViewById<EditText>(R.id.et_edit_name)
        val etEmail = view.findViewById<EditText>(R.id.et_edit_email)
        val etEmailRepeat = view.findViewById<EditText>(R.id.et_edit_email_repeat)
        val etPass = view.findViewById<EditText>(R.id.et_edit_password)
        val etPassRepeat = view.findViewById<EditText>(R.id.et_edit_password_repeat)

        val spDay = view.findViewById<Spinner>(R.id.sp_edit_day)
        val spMonth = view.findViewById<Spinner>(R.id.sp_edit_month)
        val spYear = view.findViewById<Spinner>(R.id.sp_edit_year)

        val btnSave = view.findViewById<Button>(R.id.btn_save_profile)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel_edit)

        // 2. Configurar Spinners de Fecha
        setupDateSpinners(spDay, spMonth, spYear)

        // 3. Botón Cancelar/Volver
        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        // 4. Botón Aceptar (Guardar)
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val emailRep = etEmailRepeat.text.toString()
            val pass = etPass.text.toString()
            val passRep = etPassRepeat.text.toString()

            // Validaciones simples
            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena los campos obligatorios", Toast.LENGTH_SHORT).show()
            } else if (email != emailRep) {
                Toast.makeText(requireContext(), "Los correos no coinciden", Toast.LENGTH_SHORT).show()
            } else if (pass.isNotEmpty() && pass != passRep) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Aquí irá la lógica de Firebase/Room/API
                // Por ahora simulamos éxito
                Toast.makeText(requireContext(), "Perfil actualizado (Simulado)", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun setupDateSpinners(spDay: Spinner, spMonth: Spinner, spYear: Spinner) {
        // Días (1 al 31)
        val days = (1..31).map { it.toString() }
        val dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, days)
        spDay.adapter = dayAdapter

        // Meses
        val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
        spMonth.adapter = monthAdapter

        // Años (de 2026 hacia atrás, por ejemplo hasta 1950)
        val years = (2026 downTo 1950).map { it.toString() }
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)
        spYear.adapter = yearAdapter
    }
}
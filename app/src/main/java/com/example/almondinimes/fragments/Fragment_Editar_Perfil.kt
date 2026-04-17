package com.example.almondinimes.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.almondinimes.R
import com.example.almondinimes.data.AuthManager
import java.util.*

class Fragment_Editar_Perfil : Fragment(R.layout.fragment_editar_perfil) {

    private val authManager = AuthManager()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Referencias de UI
        val etName = view.findViewById<EditText>(R.id.et_edit_name)
        val etEmail = view.findViewById<EditText>(R.id.et_edit_email)
        val etEmailRepeat = view.findViewById<EditText>(R.id.et_edit_email_repeat)
        val etPass = view.findViewById<EditText>(R.id.et_edit_password)
        val etPassRepeat = view.findViewById<EditText>(R.id.et_edit_password_repeat)

        // Desactivamos por ahora email y pass para centrarnos en perfil (Firestore)
        etEmail.isEnabled = false
        etEmailRepeat.isEnabled = false
        etPass.isEnabled = false
        etPassRepeat.isEnabled = false

        val spDay = view.findViewById<Spinner>(R.id.sp_edit_day)
        val spMonth = view.findViewById<Spinner>(R.id.sp_edit_month)
        val spYear = view.findViewById<Spinner>(R.id.sp_edit_year)

        val btnSave = view.findViewById<Button>(R.id.btn_save_profile)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel_edit)

        // 2. Configurar Spinners de Fecha con color blanco forzado
        setupDateSpinners(spDay, spMonth, spYear)

        // 3. Cargar datos actuales de Firebase
        cargarDatosActuales(etName, spDay, spMonth, spYear)

        // 4. Botón Cancelar/Volver
        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        // 5. Botón Aceptar (Guardar)
        btnSave.setOnClickListener {
            val name = etName.text.toString()

            val day = spDay.selectedItem.toString().toInt()
            val month = spMonth.selectedItemPosition
            val year = spYear.selectedItem.toString().toInt()

            // Validaciones simples
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "El nick no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else {
                val age = calculateAge(day, month, year)
                val birthDate = String.format("%02d/%02d/%04d", day, month + 1, year)

                authManager.updateProfile(name, age, birthDate) { success, error ->
                    if (success) {
                        Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun cargarDatosActuales(etName: EditText, spDay: Spinner, spMonth: Spinner, spYear: Spinner) {
        val uid = authManager.getCurrentUserUid()
        if (uid != null) {
            authManager.getUserData(uid) { usuario ->
                if (usuario != null) {
                    etName.setText(usuario.nick)

                    // Intentamos parsear la fecha: "DD/MM/YYYY"
                    if (usuario.birthDate.isNotEmpty()) {
                        try {
                            val parts = usuario.birthDate.split("/")
                            if (parts.size == 3) {
                                val day = parts[0].toInt()
                                val month = parts[1].toInt() - 1
                                val year = parts[2]

                                // Seleccionar en los spinners
                                (spDay.adapter as? ArrayAdapter<String>)?.getPosition(day.toString())?.let { spDay.setSelection(it) }
                                spMonth.setSelection(month)
                                (spYear.adapter as? ArrayAdapter<String>)?.getPosition(year)?.let { spYear.setSelection(it) }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun setupDateSpinners(spDay: Spinner, spMonth: Spinner, spYear: Spinner) {

        // Días (1 al 31)
        val days = (1..31).map { it.toString() }
        spDay.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, days).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Meses
        val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
        spMonth.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Años (Desde hoy hacia atrás 100 años)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear downTo 1950).map { it.toString() }
        spYear.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun calculateAge(day: Int, month: Int, year: Int): Int {
        val today = Calendar.getInstance()
        val birthDate = Calendar.getInstance()
        birthDate.set(year, month, day)
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }
}

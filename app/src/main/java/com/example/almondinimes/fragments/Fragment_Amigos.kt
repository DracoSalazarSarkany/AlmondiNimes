package com.example.almondinimes.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.adapters.AmigosAdapter
import com.example.almondinimes.models.Usuario
import com.example.almondinimes.viewmodels.AmigosViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.Toast
import com.example.almondinimes.R
import com.example.almondinimes.adapters.BusquedaAmigosAdapter

class Fragment_Amigos : Fragment(R.layout.fragment_amigos) {

    // Instancia compartida del ViewModel
    private val viewModel: AmigosViewModel by activityViewModels()

    private lateinit var adapterPrincipal: AmigosAdapter
    private lateinit var layoutEmpty: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Enlazar componentes
        val rvAmigos = view.findViewById<RecyclerView>(R.id.rv_amigos)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fab_add_friend)
        layoutEmpty = view.findViewById(R.id.layout_empty)

        // 2. Configurar el RecyclerView principal
        adapterPrincipal = AmigosAdapter(emptyList()) { amigo ->
            val bundle = Bundle().apply {
                putString("nick", amigo.nick)
                putInt("idNum", amigo.idNumerico)
            }
            findNavController().navigate(R.id.perfilAmigoFragment, bundle)
        }

        rvAmigos.layoutManager = LinearLayoutManager(requireContext())
        rvAmigos.adapter = adapterPrincipal

        // 3. OBSERVAR el ViewModel
        viewModel.listaAmigos.observe(viewLifecycleOwner) { listaActualizada ->
            adapterPrincipal.updateData(listaActualizada)
            checkEmptyState(listaActualizada)
        }

        // 4. Botón para añadir
        fabAdd.bringToFront()
        fabAdd.setOnClickListener { mostrarDialogoAniadir() }
    }

    private fun mostrarDialogoAniadir() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_friend, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
        builder.setView(dialogView)
        val dialog = builder.create()

        val etSearch = dialogView.findViewById<EditText>(R.id.et_search_friend_id)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm_add_friend)
        val rvResults = dialogView.findViewById<RecyclerView>(R.id.rv_search_results)

        rvResults.layoutManager = LinearLayoutManager(requireContext())
        
        // Usamos el NUEVO adapter de búsqueda con lista vacía inicial
        val adapterBusqueda = BusquedaAmigosAdapter(emptyList())
        rvResults.adapter = adapterBusqueda

        // Escuchamos mientras el usuario escribe para filtrar en tiempo real
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val resultados = viewModel.buscarUsuarios(s.toString())
                adapterBusqueda.updateData(resultados)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnConfirm.setOnClickListener {
            // Añadimos TODOS los usuarios marcados en el adapter
            if (adapterBusqueda.seleccionados.isNotEmpty()) {
                viewModel.añadirAmigos(adapterBusqueda.seleccionados)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Selecciona al menos un amigo", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()

        // Forzar el ancho del diálogo para que no se vea "aplastado"
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        // Opcional: Hacer el fondo del diálogo transparente para que se vea el redondeado de nuestro CardView
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    /**
     * Ahora recibe la lista del observer para decidir si mostrar el empty state
     */
    private fun checkEmptyState(lista: List<Usuario>) {
        layoutEmpty.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
    }
}
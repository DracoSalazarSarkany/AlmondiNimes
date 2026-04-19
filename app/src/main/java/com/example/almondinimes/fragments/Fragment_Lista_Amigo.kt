package com.example.almondinimes.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.adapters.MyObrasAdapter
import com.example.almondinimes.models.ObraGuardada
import com.example.almondinimes.viewmodels.AmigosViewModel

class Fragment_Lista_Amigo : Fragment(R.layout.layout_lista_obras_usuario) {

    private val viewModel: AmigosViewModel by activityViewModels()
    private lateinit var adapter: MyObrasAdapter
    private var fullList: List<ObraGuardada> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar argumentos
        val idUsuario = arguments?.getInt("idUsuario") ?: 0
        val nick = arguments?.getString("nick") ?: "Usuario"
        val tipo = arguments?.getString("tipo") ?: "ANIME"

        // Referencias UI
        val rvObras = view.findViewById<RecyclerView>(R.id.rv_my_obras)
        val etSearch = view.findViewById<EditText>(R.id.et_search_my_obra)
        val spSort = view.findViewById<Spinner>(R.id.sp_sort_my_obra)
        val layoutEmpty = view.findViewById<View>(R.id.layout_empty)
        val tvEmptyDesc = view.findViewById<TextView>(R.id.tv_empty_desc)
        
        // Configurar UI según tipo
        etSearch.hint = "Buscar en la lista de $nick..."
        tvEmptyDesc.text = if (tipo == "ANIME") {
            "$nick aún no tiene animes en su lista."
        } else {
            "$nick aún no tiene mangas en su lista."
        }

        // 1. Configurar Adaptador (Clic para ir al detalle)
        adapter = MyObrasAdapter(emptyList()) { obra ->
            val bundle = Bundle().apply {
                putInt("mal_id", obra.malId)
                putString("tipo", obra.type)
                putString("titulo", obra.title)
                putString("url_imagen", obra.imageUrl)
            }
            findNavController().navigate(R.id.action_listaAmigo_to_detalleObra, bundle)
        }
        rvObras.layoutManager = LinearLayoutManager(requireContext())
        rvObras.adapter = adapter

        // 2. Configurar Spinner de Ordenación
        val sortOptions = listOf("Título A-Z", "Título Z-A", "Nota (Máx)", "Nota (Min)")
        val sortAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, sortOptions)
        sortAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)
        spSort.adapter = sortAdapter

        // 3. Observar las obras del amigo
        viewModel.obrasDeOtro.observe(viewLifecycleOwner) { lista ->
            fullList = lista
            filterAndSort(etSearch.text.toString(), spSort.selectedItem.toString())
        }

        // 4. Listeners para búsqueda y ordenación
        etSearch.addTextChangedListener { text ->
            filterAndSort(text.toString(), spSort.selectedItem.toString())
        }

        spSort.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterAndSort(etSearch.text.toString(), sortOptions[position])
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        // 5. Cargar datos
        viewModel.cargarObrasDeOtro(nick, idUsuario, tipo)
    }

    private fun filterAndSort(query: String, sortType: String) {
        var result = if (query.isEmpty()) fullList else fullList.filter { it.title.contains(query, ignoreCase = true) }

        result = when (sortType) {
            "Título A-Z" -> result.sortedBy { it.title }
            "Título Z-A" -> result.sortedByDescending { it.title }
            "Nota (Máx)" -> result.sortedByDescending { it.score ?: -1.0 }
            "Nota (Min)" -> result.sortedBy { it.score ?: 11.0 }
            else -> result
        }

        adapter.updateData(result)
        view?.findViewById<View>(R.id.layout_empty)?.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
    }
}

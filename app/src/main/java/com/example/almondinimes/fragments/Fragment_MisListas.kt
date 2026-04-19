package com.example.almondinimes.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.almondinimes.R
import com.example.almondinimes.adapters.MyObrasAdapter
import com.example.almondinimes.models.ObraGuardada
import com.example.almondinimes.viewmodels.ObrasViewModel

class Fragment_MisListas : Fragment(R.layout.layout_lista_obras_usuario) {

    private val viewModel: ObrasViewModel by activityViewModels()
    private lateinit var adapter: MyObrasAdapter
    private var fullList: List<ObraGuardada> = emptyList()
    private var tipoObra: String = "ANIME"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tipoObra = arguments?.getString("tipo") ?: "ANIME"

        val rvObras = view.findViewById<RecyclerView>(R.id.rv_my_obras)
        val etSearch = view.findViewById<EditText>(R.id.et_search_my_obra)
        val spSort = view.findViewById<Spinner>(R.id.sp_sort_my_obra)

        // Configurar el texto del estado vacío
        val emptyDesc = view.findViewById<TextView>(R.id.tv_empty_desc)
        if (tipoObra == "ANIME") {
            etSearch.hint = "Buscar en mis animes..."
            emptyDesc.text = "¡Aún no tienes animes en tu lista! Empieza a añadir algunos."
        } else {
            etSearch.hint = "Buscar en mis mangas..."
            emptyDesc.text = "¡Aún no tienes mangas en tu lista! Empieza a añadir algunos."
        }

        adapter = MyObrasAdapter(emptyList()) { obra ->
            mostrarDialogoEditarItem(obra)
        }
        rvObras.layoutManager = LinearLayoutManager(requireContext())
        rvObras.adapter = adapter

        val sortOptions = listOf("Título A-Z", "Título Z-A", "Nota (Máx)", "Nota (Min)")
        val sortAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, sortOptions)
        sortAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)
        spSort.adapter = sortAdapter

        val liveData = if (tipoObra == "ANIME") viewModel.listaAnimes else viewModel.listaMangas
        liveData.observe(viewLifecycleOwner) { lista ->
            fullList = lista
            filterAndSort(etSearch.text.toString(), spSort.selectedItem.toString())
        }

        etSearch.addTextChangedListener { text ->
            filterAndSort(text.toString(), spSort.selectedItem.toString())
        }

        spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterAndSort(etSearch.text.toString(), sortOptions[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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

    private fun mostrarDialogoEditarItem(obra: ObraGuardada) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_item, null)
        
        // Usamos un tema oscuro para el diálogo para que los Spinners se vean bien
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
        builder.setView(dialogView).create().apply {
            val tvObraName = dialogView.findViewById<TextView>(R.id.tv_edit_obra_name)
            val spScore = dialogView.findViewById<Spinner>(R.id.sp_edit_score)
            val spStatus = dialogView.findViewById<Spinner>(R.id.sp_edit_status)
            
            tvObraName.text = obra.title
            val scores = listOf("Sin puntuar") + (1..10).map { it.toString() }
            val statuses = listOf("Viendo", "Completado", "Pendiente", "Dropeado")

            // Usamos nuestros layouts personalizados para asegurar visibilidad en modo oscuro
            val scoreAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, scores)
            scoreAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)
            spScore.adapter = scoreAdapter

            val statusAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, statuses)
            statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)
            spStatus.adapter = statusAdapter

            // Posicionar en los valores actuales
            val currentScoreStr = itemScoreToString(obra.score)
            val scorePos = scores.indexOf(currentScoreStr).coerceAtLeast(0)
            spScore.setSelection(scorePos)
            
            val statusPos = statuses.indexOf(obra.status).coerceAtLeast(0)
            spStatus.setSelection(statusPos)

            dialogView.findViewById<Button>(R.id.btn_edit_delete).setOnClickListener { 
                viewModel.eliminarObra(obra)
                dismiss() 
            }
            dialogView.findViewById<Button>(R.id.btn_edit_save).setOnClickListener {
                val selectedScoreStr = spScore.selectedItem.toString()
                val newScore = if (selectedScoreStr == "Sin puntuar") null else selectedScoreStr.toDouble()
                viewModel.actualizarObra(obra.copy(score = newScore, status = spStatus.selectedItem.toString()))
                dismiss()
            }
            dialogView.findViewById<Button>(R.id.btn_edit_cancel).setOnClickListener { dismiss() }

            show()
            window?.setLayout((resources.displayMetrics.widthPixels * 0.90).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun itemScoreToString(score: Double?): String {
        return score?.let {
            if (it % 1.0 == 0.0) it.toInt().toString() else it.toString()
        } ?: "Sin puntuar"
    }
}

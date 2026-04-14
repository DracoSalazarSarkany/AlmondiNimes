package com.example.almondinimes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.almondinimes.models.ObraGuardada

class ObrasViewModel : ViewModel() {

    private val _listaAnimes = MutableLiveData<List<ObraGuardada>>(emptyList())
    val listaAnimes: LiveData<List<ObraGuardada>> = _listaAnimes

    private val _listaMangas = MutableLiveData<List<ObraGuardada>>(emptyList())
    val listaMangas: LiveData<List<ObraGuardada>> = _listaMangas

    fun añadirObra(obra: ObraGuardada) {
        if (obra.malId == 0) return // Protección contra IDs inválidos

        if (obra.type == "ANIME") {
            val currentList = _listaAnimes.value.orEmpty().toMutableList()
            // Evitar duplicados
            if (currentList.none { it.malId == obra.malId }) {
                currentList.add(0, obra)
                _listaAnimes.value = currentList
            }
        } else {
            val currentList = _listaMangas.value.orEmpty().toMutableList()
            if (currentList.none { it.malId == obra.malId }) {
                currentList.add(0, obra)
                _listaMangas.value = currentList
            }
        }
    }

    fun eliminarObra(obra: ObraGuardada) {
        if (obra.type == "ANIME") {
            val currentList = _listaAnimes.value.orEmpty().toMutableList()
            currentList.removeAll { it.malId == obra.malId }
            _listaAnimes.value = currentList
        } else {
            val currentList = _listaMangas.value.orEmpty().toMutableList()
            currentList.removeAll { it.malId == obra.malId }
            _listaMangas.value = currentList
        }
    }

    fun actualizarObra(obraActualizada: ObraGuardada) {
        if (obraActualizada.type == "ANIME") {
            val currentList = _listaAnimes.value.orEmpty().toMutableList()
            val index = currentList.indexOfFirst { it.malId == obraActualizada.malId }
            if (index != -1) {
                currentList[index] = obraActualizada
                _listaAnimes.value = currentList
            }
        } else {
            val currentList = _listaMangas.value.orEmpty().toMutableList()
            val index = currentList.indexOfFirst { it.malId == obraActualizada.malId }
            if (index != -1) {
                currentList[index] = obraActualizada
                _listaMangas.value = currentList
            }
        }
    }
}

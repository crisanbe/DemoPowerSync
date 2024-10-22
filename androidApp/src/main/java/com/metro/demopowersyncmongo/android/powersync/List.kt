package com.metro.demopowersyncmongo.android.powersync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.powersync.PowerSyncDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

internal class ListContent(
    private val db: PowerSyncDatabase,
    private val userId: String?
) : ViewModel() {

    private val _selectedListId = MutableStateFlow<String?>(null)
    val selectedListId: StateFlow<String?> = _selectedListId

    private val _inputText = MutableStateFlow<String>("")
    val inputText: StateFlow<String> = _inputText

    // Observa los elementos de la lista (sincronizado con MongoDB a través de PowerSync)
    fun watchItems(): Flow<List<UsoItem>> {
        return db.watch("SELECT * FROM uso", mapper = { cursor ->
            val id = cursor.getString(0) ?: ""        // Verifica que no sea null
            val name = cursor.getString(1) ?: "N/A"   // Valor por defecto si es null
            val createdAt = cursor.getString(2) ?: "N/A" // Valor por defecto si es null
            val ownerId = cursor.getString(3) ?: "N/A"   // Valor por defecto si es null

            UsoItem(
                id = id,
                name = name,
                createdAt = createdAt,
                ownerId = ownerId
            )
        })
    }


    // Elimina un elemento de la lista
    fun onItemDeleteClicked(item: UsoItem) {
        viewModelScope.launch {
            db.writeTransaction {
                it.execute("DELETE FROM uso WHERE id = ?", listOf(item.id))
            }
        }
    }

    // Agrega un nuevo elemento a la lista
    fun onAddItemClicked() {
        if (_inputText.value.isBlank()) return

        viewModelScope.launch {
            db.writeTransaction {
                it.execute(
                    "INSERT INTO uso (id, name, created_at, owner_id) VALUES (uuid(),?, datetime(), ?)",
                    listOf(_inputText.value, userId)
                )
            }
            _inputText.value = ""

            // Inicia la sincronización manualmente después de la inserción
            Logger.i("Iniciando la sincronización manual con MongoDB.")
        }

    }

    // Actualiza un elemento de la lista
    fun updateItem(itemId: String, newName: String) {
        viewModelScope.launch {
            db.execute(
                sql = "UPDATE uso SET name = ? WHERE id = ?",
                parameters = listOf(newName, itemId)
            )
        }
    }

    // Maneja la selección de un item
    fun onItemClicked(item: UsoItem) {
        _selectedListId.value = item.id
    }

    // Maneja los cambios en el texto de entrada
    fun onInputTextChanged(text: String) {
        _inputText.value = text
    }
}

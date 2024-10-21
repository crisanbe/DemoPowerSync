package com.metro.demopowersyncmongo.android.powersync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersync.PowerSyncDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
            UsoItem(
                id = cursor.getString(0)!!,  // PowerSync ya maneja la columna `id`
                name = cursor.getString(1)!!,
                createdAt = cursor.getString(2)!!,
                ownerId = cursor.getString(3)!!
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
                // Aquí solo se insertan los tres valores. El ID es gestionado por PowerSync.
                it.execute(
                    "INSERT INTO uso (name, created_at, owner_id) VALUES (?, datetime(), ?)",
                    listOf(_inputText.value, userId)
                )
            }
            _inputText.value = ""
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

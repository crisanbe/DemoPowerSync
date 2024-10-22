package com.metro.demopowersyncmongo.android.powersync

import com.powersync.db.schema.Column
import com.powersync.db.schema.Index
import com.powersync.db.schema.IndexedColumn
import com.powersync.db.schema.Schema
import com.powersync.db.schema.Table

val AppSchema: Schema = Schema(
    listOf(
        Table(
            name = "uso",
            columns = listOf(
                // No declares la columna "id", ya que es añadida automáticamente por PowerSync
                Column.text("name"),           // Nombre del elemento
                Column.text("created_at"),     // Fecha de creación
                Column.text("owner_id")        // ID del propietario
            ),
            indexes = listOf(
                Index("owner_index", listOf(IndexedColumn.descending("owner_id")))
            )
        )
    )
)

// Clase de datos para los elementos de uso
data class UsoItem(
    val id: String,
    val createdAt: String,
    val name: String,
    val ownerId: String
)

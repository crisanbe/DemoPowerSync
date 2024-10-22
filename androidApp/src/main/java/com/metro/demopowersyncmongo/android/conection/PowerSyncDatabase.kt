package com.metro.demopowersyncmongo.android.conection

import co.touchlab.kermit.Logger
import com.powersync.PowerSyncDatabase
import com.powersync.connectors.PowerSyncBackendConnector
import com.powersync.connectors.PowerSyncCredentials
import com.powersync.db.crud.UpdateType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MyConnector : PowerSyncBackendConnector() {
    private var token: String? = null

    override suspend fun fetchCredentials() = PowerSyncCredentials(
        userId = "6711631325a60ad3b2df2ad4",
        endpoint = "https://6711631325a60ad3b2df2ad4.powersync.journeyapps.com",
        token = "eyJhbGciOiJSUzI1NiIsImtpZCI6InBvd2Vyc3luYy1kZXYtMzIyM2Q0ZTMifQ.eyJzdWIiOiI2NzExNjMxMzI1YTYwYWQzYjJkZjJhZDQiLCJpYXQiOjE3Mjk2MDQ3NDMsImlzcyI6Imh0dHBzOi8vcG93ZXJzeW5jLWFwaS5qb3VybmV5YXBwcy5jb20iLCJhdWQiOiJodHRwczovLzY3MTE2MzEzMjVhNjBhZDNiMmRmMmFkNC5wb3dlcnN5bmMuam91cm5leWFwcHMuY29tIiwiZXhwIjoxNzI5NjQ3OTQzfQ.u88E28lIt7xtyzwrWjyCdr3bFW5qkPBhpiPHBPNyUusJhywuos9rYd4n2XyjM8UKJksUjSD9_bOy3oTBlKbY9CLoacwkD1UmZ4_fB7OJUwupYH2G42AO7p-FFlxgkNrq5SkdPM6fmKUWp0ygEdDonOUbeiXfpL8oa6zacgRTVrM8lCYbh5BZwAyeWsHN6gupgS3yVO_qq71yP4OTnNSztKrqA5D4Ak-Ju0Ze6miJjtZLODIfxYdrhJ2O8Yz1IG0K5CstyWnf8S2TMoHZ5p3UbNwtQ4jgWgXKjeUQWasRJdH-5WNBGSXErVPHyjR6hPmHFuN9o1jXHvIOkOxE27oniw"
    )

    override suspend fun uploadData(database: PowerSyncDatabase) {
        val transaction = database.getNextCrudTransaction() ?: return
        Logger.i("Transacciones pendientes: ${transaction.crud.size}")

        try {
            transaction.crud.forEach { entry ->
                Logger.i("Procesando transacción: ${entry.op} para la tabla ${entry.table}")

                val apiRequest = ApiRequest(
                    op = when (entry.op) {
                        UpdateType.PUT -> "PUT"
                        UpdateType.DELETE -> "DELETE"
                        UpdateType.PATCH -> "PATCH"
                        else -> throw IllegalArgumentException("Operación no soportada")
                    },
                    data = Data(
                        id = entry.id,
                        name = entry.opData?.get("name") ?: "",
                        created_at = entry.opData?.get("created_at") ?: "",
                        owner_id = entry.opData?.get("owner_id") ?: ""
                    )
                )

                val response: HttpResponse = httpClient.post("https://6m733imv0b.execute-api.us-east-1.amazonaws.com/dev/PowerSyncMongoDBFunction") {
                    contentType(ContentType.Application.Json)
                    setBody(apiRequest)
                }

                Logger.i("Respuesta de la API: ${response.body<String>()}")
            }

            transaction.complete(null)
        } catch (e: Exception) {
            Logger.e("Error en la sincronización: ${e.message}")
            transaction.complete(e.message)
        }
    }

    fun clearCredentials() {
        token = null
    }
}
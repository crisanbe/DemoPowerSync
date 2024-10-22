package com.metro.demopowersyncmongo.android.conection

import co.touchlab.kermit.Logger
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    val op: String,
    val data: Data
)

@Serializable
data class Data(
    val id: String,
    val name: String,
    val created_at: String,
    val owner_id: String
)

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String?
)

suspend fun insertOrUpdateData(apiUrl: String, request: ApiRequest): ApiResponse {
    return try {
        val response: ApiResponse = httpClient.post(apiUrl) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()  // Deserializa la respuesta a ApiResponse

        response  // Devuelve el objeto ApiResponse
    } catch (e: Exception) {
        println("Error: ${e.localizedMessage}")
        ApiResponse(success = false, message = "Error al realizar la petición: ${e.localizedMessage}")
    }
}


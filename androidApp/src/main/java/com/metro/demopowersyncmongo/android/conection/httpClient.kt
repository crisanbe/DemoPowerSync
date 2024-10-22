package com.metro.demopowersyncmongo.android.conection

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Cliente HTTP que se puede usar en todas las plataformas
val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true; isLenient = true; ignoreUnknownKeys = true })
    }
}
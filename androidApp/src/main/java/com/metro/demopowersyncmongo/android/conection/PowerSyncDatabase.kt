package com.metro.demopowersyncmongo.android.conection

//import com.mongodb.client.MongoClients
//import com.mongodb.client.model.Filters
import com.powersync.PowerSyncDatabase
import com.powersync.connectors.PowerSyncBackendConnector
import com.powersync.connectors.PowerSyncCredentials
import com.powersync.db.crud.UpdateType
//import org.bson.Document

class MyConnector : PowerSyncBackendConnector() {
    private var token: String? = null

    // Implementación del método para obtener las credenciales
    override suspend fun fetchCredentials(): PowerSyncCredentials {
        return PowerSyncCredentials(
            userId = "6711631325a60ad3b2df2ad4",
            endpoint = "https://6711631325a60ad3b2df2ad4.powersync.journeyapps.com",
            token = "eyJhbGciOiJSUzI1NiIsImtpZCI6InBvd2Vyc3luYy1kZXYtMzIyM2Q0ZTMifQ.eyJzdWIiOiI2NzExNjMxMzI1YTYwYWQzYjJkZjJhZDQiLCJpYXQiOjE3Mjk0Mzk0NzYsImlzcyI6Imh0dHBzOi8vcG93ZXJzeW5jLWFwaS5qb3VybmV5YXBwcy5jb20iLCJhdWQiOiJodHRwczovLzY3MTE2MzEzMjVhNjBhZDNiMmRmMmFkNC5wb3dlcnN5bmMuam91cm5leWFwcHMuY29tIiwiZXhwIjoxNzI5NDgyNjc2fQ.lcURS9Fq9XVfj4ayuzh_R2cax4jwHwoFnxQPpebDczkx48iZFbTYndly3jQjaLomP6rikW6OB96shU_J51GcQ1pQ0398D-TSDO_E_7KLw2zRmvHHNjh8RhfPjNkcvpnbnRJB9_Kf4b7jbZ_H-HRu3ySEW2F4Dsg5AJRuyOhdDr-j5ZRVRywrSQgJEr18NWB04tk69WK1HWKFNq258L6IrUotj2qRTJkTvpcfigX_VhxhOKWyJHXv56dovYxkRkLJs-lGsA18zxQ9-9YfHzlHiHoc-rSiO0oNLbJIbx2mhjQdYUyjj1gEkwFyySA0W4X7WhoL5JXFunySEEeJ0VgRUA"  // Reemplaza con tu token
        )
    }

    // Implementación del método para sincronizar los datos locales con MongoDB
    override suspend fun uploadData(database: PowerSyncDatabase) {
        val transaction = database.getNextCrudTransaction() ?: return

        try {

            // Marca la transacción como completada después de subir los datos
            transaction.complete(null)

        } catch (e: Exception) {
            // Manejo de errores y reintentos si es necesario
            transaction.complete(e.message)
            //throw e
        }
    }

    fun clearCredentials() {
        // Limpia el token almacenado
        token = null
    }
}

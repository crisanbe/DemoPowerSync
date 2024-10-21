package com.metro.demopowersyncmongo.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.powersync.PowerSyncDatabase
import com.metro.demopowersyncmongo.android.conection.MyConnector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    data object SignedOut: AuthState()
    data object SignedIn: AuthState()
}

internal class AuthViewModel(
    private val connector: MyConnector,  // Usa tu conector personalizado en lugar de Supabase
    private val db: PowerSyncDatabase,
    private val navController: NavController
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val authState: StateFlow<AuthState> = _authState
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    init {
        // Comienza la autenticación cuando se inicia la ViewModel
        viewModelScope.launch {
            try {
                // Autentica usando el conector personalizado (MyConnector)
                val credentials = connector.fetchCredentials()  // Obtiene las credenciales
                _authState.value = AuthState.SignedIn
                _userId.value = credentials.userId  // Establece el userId a partir de las credenciales obtenidas

                // Conecta la base de datos local a PowerSync usando las credenciales
                db.connect(connector)

                // Navega a la pantalla principal si está autenticado
                navController.navigate(Screen.Home)
            } catch (e: Exception) {
                Logger.e("Error during authentication: $e")
                _authState.value = AuthState.SignedOut
            }
        }
    }

    suspend fun signIn(token: String) {
        try {
            // Usa fetchCredentials para manejar el token y obtener las credenciales necesarias
            val credentials = connector.fetchCredentials()

            _authState.value = AuthState.SignedIn
            _userId.value = credentials.userId  // Establece el userId a partir de las credenciales obtenidas
        } catch (e: Exception) {
            Logger.e("Error during sign-in: $e")
            _authState.value = AuthState.SignedOut
        }
    }


    // Nueva función para manejar la salida de sesión en una corrutina
    fun signOutAsync() {
        viewModelScope.launch {
            try {
                db.disconnectAndClear()  // Desconecta la base de datos y limpia los datos locales
                connector.clearCredentials()  // Limpia las credenciales de autenticación
                _authState.value = AuthState.SignedOut
                navController.navigate(Screen.SignIn)
            } catch (e: Exception) {
                Logger.e("Error signing out: $e")
            }
        }
    }
}

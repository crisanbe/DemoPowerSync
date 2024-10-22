package com.metro.demopowersyncmongo.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import com.powersync.PowerSyncDatabase
import com.metro.demopowersyncmongo.android.conection.MyConnector
import com.metro.demopowersyncmongo.android.powersync.AppSchema
import com.metro.demopowersyncmongo.android.powersync.ListContent
import com.metro.demopowersyncmongo.android.screens.HomeScreen
import com.metro.demopowersyncmongo.android.screens.SignInScreen
import com.metro.demopowersyncmongo.android.screens.SignUpScreen
import com.powersync.compose.rememberDatabaseDriverFactory

@Composable
fun App() {
    val driverFactory = rememberDatabaseDriverFactory()
    val powerSync = remember { MyConnector() }
    val db = remember { PowerSyncDatabase(driverFactory, schema = AppSchema) }
    val status by db.currentStatus.asFlow().collectAsState(initial = null)

    LaunchedEffect(Unit) {
        db.connect(powerSync)
        Logger.i("Intentando conectar a PowerSync")
        db.currentStatus.asFlow().collect { status ->
            Logger.i(if (status.connected) "Conectado a PowerSync." else "Desconectado de PowerSync.")
            if (status.uploading) Logger.i("Sincronizando datos locales a MongoDB.")
            status.downloadError?.let { Logger.e("Error al descargar datos desde MongoDB: $it") }
        }
    }

    val navController = remember { NavController(Screen.Home) }
    val authViewModel = remember { AuthViewModel(powerSync, db, navController) }
    val authState by authViewModel.authState.collectAsState()
    val currentScreen by navController.currentScreen.collectAsState()

    when (currentScreen) {
        is Screen.Home -> {
            if (authState == AuthState.SignedOut) navController.navigate(Screen.SignIn)
            val lists = remember { ListContent(db, authViewModel.userId.value) }
            val items by lists.watchItems().collectAsState(initial = emptyList())
            val listsInputText by lists.inputText.collectAsState()

            HomeScreen(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                items = items,
                inputText = listsInputText,
                isConnected = status?.connected,
                onSignOutSelected = { authViewModel.signOutAsync() },
                onItemClicked = { lists.onItemClicked(it) },
                onItemDeleteClicked = { lists.onItemDeleteClicked(it) },
                onAddItemClicked = { lists.onAddItemClicked() },
                onInputTextChanged = { lists.onInputTextChanged(it) }
            )
        }
        is Screen.SignIn -> {
            if (authState == AuthState.SignedIn) navController.navigate(Screen.Home)
            SignInScreen(navController, authViewModel)
        }
        else -> SignUpScreen(navController, authViewModel)
    }

    if (status?.connected == true) {
        when {
            status?.downloading == true -> Text("Descargando datos desde MongoDB...")
            status?.uploading == true -> Text("Subiendo datos a MongoDB...")
            else -> Text("Sincronización completa.")
        }
    } else {
        Text("Sin conexión. Datos en modo offline.")
    }
}
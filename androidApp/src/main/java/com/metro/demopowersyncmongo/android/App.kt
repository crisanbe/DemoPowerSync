package com.metro.demopowersyncmongo.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.powersync.PowerSyncDatabase
import com.metro.demopowersyncmongo.android.conection.MyConnector
import com.metro.demopowersyncmongo.android.powersync.AppSchema
import com.metro.demopowersyncmongo.android.powersync.ListContent
import com.metro.demopowersyncmongo.android.screens.HomeScreen
import com.metro.demopowersyncmongo.android.screens.SignInScreen
import com.metro.demopowersyncmongo.android.screens.SignUpScreen
import com.powersync.compose.rememberDatabaseDriverFactory
import com.powersync.db.schema.Schema

@Composable
fun App() {
    // Inicializa la fábrica de controladores de base de datos
    val driverFactory = rememberDatabaseDriverFactory()

    // Usa el conector personalizado `MyConnector`
    val powerSync = remember { MyConnector() }

    // Inicializa la base de datos local usando el esquema
    val db = remember { PowerSyncDatabase(driverFactory, schema = AppSchema) }

    // Monitorea el estado de sincronización (online/offline)
    val syncStatus = db.currentStatus
    val status = syncStatus.asFlow().collectAsState(initial = null)

    // Conectar la base de datos a PowerSync en la primera ejecución
    LaunchedEffect(Unit) {
        db.connect(powerSync)  // Aquí se usa tu conector personalizado para conectar a MongoDB
    }

    // Configura el controlador de navegación
    val navController = remember { NavController(Screen.Home) }

    // Inicializa el ViewModel de autenticación
    val authViewModel = remember {
        AuthViewModel(powerSync, db, navController)
    }

    // Observa el estado de autenticación
    val authState by authViewModel.authState.collectAsState()
    val currentScreen by navController.currentScreen.collectAsState()

    // Maneja la navegación basada en el estado de autenticación
    when (currentScreen) {
        is Screen.Home -> {
            if (authState == AuthState.SignedOut) {
                navController.navigate(Screen.SignIn)
            }

            // Manejo de listas y tareas
            val lists = remember { ListContent(db, authViewModel.userId.value) }  // Usa la clase ListContent
            val items by lists.watchItems().collectAsState(initial = emptyList())
            val listsInputText by lists.inputText.collectAsState()

            HomeScreen(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                items = items,  // Lista de elementos (items), obtenidos de la base de datos MongoDB
                inputText = listsInputText,  // Texto de entrada actual
                isConnected = status.value?.connected,  // Mostrar si está conectado o no
                onSignOutSelected = { authViewModel.signOutAsync() },  // Manejar el cierre de sesión
                onItemClicked = { item -> lists.onItemClicked(item) },  // Lógica para manejar cuando se selecciona un item
                onItemDeleteClicked = { item -> lists.onItemDeleteClicked(item) },  // Lógica para manejar cuando se elimina un item
                onAddItemClicked = { lists.onAddItemClicked() },  // Lógica para manejar cuando se agrega un nuevo item
                onInputTextChanged = { text -> lists.onInputTextChanged(text) }  // Lógica para manejar cambios en el texto de entrada
            )

        }

        is Screen.SignIn -> {
            if (authState == AuthState.SignedIn) {
                navController.navigate(Screen.Home)
            }

            SignInScreen(navController, authViewModel)
        }

        else -> {
            SignUpScreen(navController, authViewModel)
        }
    }
}

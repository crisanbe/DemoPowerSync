package com.metro.demopowersyncmongo.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metro.demopowersyncmongo.android.NavController
import com.metro.demopowersyncmongo.android.Screen
import com.metro.demopowersyncmongo.android.components.Input
import com.metro.demopowersyncmongo.android.components.TodoList
import com.metro.demopowersyncmongo.android.components.WifiIcon
import com.metro.demopowersyncmongo.android.powersync.UsoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TodosScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    items: List<UsoItem>,
    inputText: String,
    isConnected: Boolean?,
    onItemClicked: (item: UsoItem) -> Unit,
    onItemDoneChanged: (item: UsoItem, isDone: Boolean) -> Unit,
    onItemDeleteClicked: (item: UsoItem) -> Unit,
    onAddItemClicked: () -> Unit,
    onInputTextChanged: (value: String) -> Unit,
) {
    Column(modifier) {
        TopAppBar(
            title = {
                Text(
                    "Todo List",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(end = 36.dp)
                ) },
            navigationIcon = {
                IconButton(onClick = { navController.navigate(Screen.Home) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                }
            },
            actions = {
                WifiIcon(isConnected ?: false)
                Spacer(modifier = Modifier.width(16.dp))
            }
        )

        Input(
            text = inputText,
            onAddClicked = onAddItemClicked,
            onTextChanged = onInputTextChanged,
            screen = Screen.Todos
        )

        Box(Modifier.weight(1F)) {
            TodoList(
                items = items,
                onItemClicked = onItemClicked,
                onItemDoneChanged = onItemDoneChanged,
                onItemDeleteClicked = onItemDeleteClicked
            )
        }
    }
}

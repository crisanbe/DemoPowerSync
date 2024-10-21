package com.metro.demopowersyncmongo.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metro.demopowersyncmongo.android.Screen
import com.metro.demopowersyncmongo.android.components.Input
import com.metro.demopowersyncmongo.android.components.ListContent
import com.metro.demopowersyncmongo.android.components.Menu
import com.metro.demopowersyncmongo.android.components.WifiIcon
import com.metro.demopowersyncmongo.android.powersync.UsoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    items: List<UsoItem>,
    inputText: String,
    isConnected: Boolean?,
    onSignOutSelected: () -> Unit,
    onItemClicked: (item: UsoItem) -> Unit,
    onItemDeleteClicked: (item: UsoItem) -> Unit,
    onAddItemClicked: () -> Unit,
    onInputTextChanged: (value: String) -> Unit,
) {

    Column(modifier) {
        TopAppBar(
            title = {
                Text(
                "Todo Lists",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(end = 36.dp)
            ) },
            navigationIcon = { Menu(
                true,
                onSignOutSelected
            ) },
            actions = {
                WifiIcon(isConnected ?: false)
                Spacer(modifier = Modifier.width(16.dp))
            }
        )

        Input(
            text = inputText,
            onAddClicked = onAddItemClicked,
            onTextChanged = onInputTextChanged,
            screen = Screen.Home
        )

        Box(Modifier.weight(1F)) {
            ListContent(
                items = items,
                onItemClicked = onItemClicked,
                onItemDeleteClicked = onItemDeleteClicked
            )
        }
    }
}

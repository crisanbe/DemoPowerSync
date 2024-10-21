package com.metro.demopowersyncmongo.android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.metro.demopowersyncmongo.android.powersync.UsoItem
import com.metro.demopowersyncmongo.android.util.VerticalScrollbar
import com.metro.demopowersyncmongo.android.util.rememberScrollbarAdapter

@Composable
internal fun ListContent(
    items: List<UsoItem>,
    onItemClicked: (item: UsoItem) -> Unit,
    onItemDeleteClicked: (item: UsoItem) -> Unit,
) {
    Box {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(items) { item ->
                ListItem(
                    item = item,
                    onClicked = { onItemClicked(item) },
                    onDeleteClicked = { onItemDeleteClicked(item) }
                )

                HorizontalDivider()
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = listState)
        )
    }
}

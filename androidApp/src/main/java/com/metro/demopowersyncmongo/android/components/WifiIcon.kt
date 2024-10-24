package com.metro.demopowersyncmongo.android.components

import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable

@Composable
fun WifiIcon(isConnected: Boolean) {
    val icon = if (isConnected) {
        Icons.Filled.Wifi
    } else {
        Icons.Filled.WifiOff
    }

    Icon(
        imageVector = icon,
        contentDescription = if (isConnected) "Online" else "Offline",
    )
}
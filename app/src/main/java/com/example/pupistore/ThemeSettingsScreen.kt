package com.example.pupistore

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ThemeSettingsScreen() {
    val context = LocalContext.current
    val isDark by getTheme(context).collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    Row(modifier = Modifier.padding(16.dp)) {
        Text("Tema oscuro")
        Switch(
            checked = isDark,
            onCheckedChange = {
                scope.launch {
                    saveTheme(it, context)
                }
            }
        )
    }
}

package com.example.oriencoop_score.view.pantalla_principal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.oriencoop_score.model.MovimientosLcc
import com.example.oriencoop_score.model.Notifications
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.DetailRow
import com.example.oriencoop_score.view_model.NotificationViewModel

@Composable
fun NotificationDialog(
    viewModel: NotificationViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Notifications") },
        text = {
            if (viewModel.notifications.isEmpty()) {
                Text("No notifications yet.")
            } else {
                LazyColumn {
                    items(viewModel.notifications) { notification ->
                        NotificationItem(notification)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Close")
            }
        },
        dismissButton = {
            if (viewModel.notifications.isNotEmpty()){
                TextButton(onClick = { viewModel.clearNotifications() }) {
                    Text("Clear All", color = Color.Red)
                }
            }
        }
    )
}

@Composable
fun NotificationItem(notification: Notifications) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = notification.TITULO, style = AppTheme.typography.titulos)
            Text(text = notification.DESCRIPCION, style = AppTheme.typography.normal)
            Text(text = "Date: ${notification.DATE}, Time: ${notification.TIME}", style = AppTheme.typography.normal)
        }
    }
}

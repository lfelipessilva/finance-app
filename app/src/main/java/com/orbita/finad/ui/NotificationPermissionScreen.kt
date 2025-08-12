package com.orbita.finad.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orbita.finad.services.NotificationPermissionChecker

@Composable
fun NotificationPermissionScreen(onPermissionGranted: () -> Unit) {
  val context = LocalContext.current

  LaunchedEffect(Unit) {
    while (true) {
      if (NotificationPermissionChecker.isNotificationPermissionGranted(context)) {
        onPermissionGranted()
        break
      }
      kotlinx.coroutines.delay(500)
    }
  }

  Column(
          modifier =
                  Modifier.fillMaxSize()
                          .background(MaterialTheme.colorScheme.background)
                          .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
  ) {
    Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
            text = "Permissão Necessária",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
    )

    Text(
            text =
                    "O Finad precisa acessar suas notificações para detectar gastos automaticamente.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
    )

    Button(
            onClick = {
              val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
              context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
    ) { Text("Habilitar") }

    Spacer(modifier = Modifier.height(16.dp))

    TextButton(onClick = { onPermissionGranted() }) { Text("Pular") }
  }
}

package com.example.oriencoop_score
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.oriencoop_score.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.HiltAndroidApp


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val REQUEST_CODE_NOTIFICATION_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
/*********Agregar ruta al presionar la notificación************************************
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                Navigation(initialRoute = getInitialRouteFromIntent(intent))
            }
        }

        private fun getInitialRouteFromIntent(intent: Intent): String {
            return if (intent.hasExtra("NAVIGATE_TO")) {
                intent.getStringExtra("NAVIGATE_TO") ?: "Login" // Default to login
            } else {
                "Login" // Default route
            }
        }*/

        // Check and request notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_NOTIFICATION_PERMISSION
                )
            }
        }


    }
}
/*
//Llamada vista composable de login por viewmode
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
        ScreenMain()
}
*/

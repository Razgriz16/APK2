package com.example.oriencoop_score
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.oriencoop_score.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Login(com.example.oriencoop_score.view_model.login.LoginViewModel())
            //PantallaPrincipal()
            Navigation()

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

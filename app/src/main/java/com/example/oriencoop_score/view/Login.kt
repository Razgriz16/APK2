package com.example.oriencoop_score.view
import com.example.oriencoop_score.view_model.login.LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.utility.LoginState
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.R

@Composable
fun Login(navController: NavController) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) { LoginScreen(navController)}
}

//@Preview
@Composable
fun LoginScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginState.collectAsState()
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    var buttonEnabled by remember { mutableStateOf(true) }
    val rutValid by loginViewModel.rutValid.collectAsState()

    // Use LaunchedEffect to trigger navigation only when loginState changes to Success
    LaunchedEffect(key1 = loginState) {
        if (loginState is LoginState.Success) {
            navController.navigate(Pantalla.PantallaPrincipal.route)


        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo and other UI components
        Image(painter = painterResource(id = R.drawable.logooriencoop), contentDescription = "Logo")
        Spacer(modifier = Modifier.height(5.dp))
        Card(modifier = Modifier.fillMaxWidth(0.9f)) {
            Column(
                modifier = Modifier
                    .background(color = Color(0xFF006FB6))
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Inicia sesión", fontSize = 20.sp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Username TextField
                    TextField(
                        value = username,
                        onValueChange = { loginViewModel.updateUsername(it) },
                        label = { Text("Rut (12345678-9)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Password TextField
                    TextField(
                        value = password,
                        onValueChange = { loginViewModel.updatePassword(it) },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Login Button
                    Button(
                        onClick = {
                            loginViewModel.performLogin(username, password)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf49600)),
                        enabled = loginState !is LoginState.Loading && username.isNotEmpty() //&& password.isNotEmpty(), // Combine states
                    ) {
                        Text("Log In")
                    }

                    // Display RUT validation message
                    if (rutValid == false){
                        Text(
                            text = "Rut incorrecto",
                            color = Color.Red
                        )
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                    // Optionally show a progress indicator or error message here


                    when (loginState) {
                        is LoginState.Loading -> CircularProgressIndicator()
                        is LoginState.Error -> Text(
                            text = "Rut o contraseña incorrectos",
                            color = Color.Red
                        )
                        else -> {}
                    }
                }
            }
        }
    }
}



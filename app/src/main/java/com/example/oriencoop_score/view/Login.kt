package com.example.oriencoop_score.view
import com.example.oriencoop_score.view_model.login.LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    val focusManager = LocalFocusManager.current // Para manejar el foco

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable { focusManager.clearFocus() } // Clic fuera cierra el teclado
        ,
        contentAlignment = Alignment.Center
    ) {
        LoginScreen(navController)
    }
}


@Composable
fun LoginScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginState.collectAsState()
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val rutValid by loginViewModel.rutValid.collectAsState()

    // Focus Requesters para los campos de texto
    val rutFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var rutHasFocus by remember { mutableStateOf(false) }


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
        Image(
            painter = painterResource(id = R.drawable.oriencoop_logo_new),
            contentDescription = "Logo",
            modifier = Modifier.padding(bottom = 16.dp)
        )
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
                        onValueChange = { rawInput ->
                            //  1. Remove all whitespace and newlines
                            val cleanedInput = rawInput.replace("\\s+".toRegex(), "")
                            //  2. Update the ViewModel with the cleaned input.
                            loginViewModel.updateUsername(cleanedInput)
                        },
                        label = {
                            if (!rutHasFocus && username.isEmpty()) { // Placeholder condicional
                                Text("Rut (12345678-9)")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(rutFocusRequester) // Asocia el FocusRequester
                            .onFocusChanged { rutHasFocus = it.hasFocus }, //Detecta el focus
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next, // Cambia a "Next"
                            keyboardType = KeyboardType.Text // Asegúrate de que sea un teclado de texto
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { passwordFocusRequester.requestFocus() } // Mueve el foco al campo de contraseña
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Password TextField
                    TextField(
                        value = password,
                        onValueChange = { loginViewModel.updatePassword(it) },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester), // Asocia el FocusRequester
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done, // "Done" en lugar de "Next"
                            keyboardType = KeyboardType.Password //Tipo password
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() } //Oculta el teclado
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Login Button
                    Button(
                        onClick = {
                            focusManager.clearFocus() // Importante:  cierra teclado antes de login
                            loginViewModel.performLogin(username, password)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf49600)),
                        enabled = loginState !is LoginState.Loading && username.isNotEmpty()
                    ) {
                        Text("Log In")
                    }

                    if (rutValid == false) {
                        Text(
                            text = "Rut incorrecto",
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
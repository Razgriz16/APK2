package com.example.oriencoop_score.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.R
import androidx.compose.ui.res.painterResource
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.view_model.LoginViewModel
import com.example.oriencoop_score.utility.cleanRut
import com.example.oriencoop_score.utility.validRut

@Composable
fun Login(navController: NavController) {
    val focusManager = LocalFocusManager.current // Para manejar el foco

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable { focusManager.clearFocus() }, // Clic fuera cierra el teclado
        contentAlignment = Alignment.Center
    ) {
        LoginScreen(navController)
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val uiState by loginViewModel.uiState.collectAsState()

    // Estados locales para los campos de texto
    var rut by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rutValid by remember { mutableStateOf(true) }
    var rutHasFocus by remember { mutableStateOf(false) }

    // Focus Requesters para los campos de texto
    val rutFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Navegación en caso de éxito
    LaunchedEffect(key1 = uiState) {
        if (uiState is LoginViewModel.LoginUiState.Success) {
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
                    // RUT TextField
                    TextField(
                        value = rut,
                        onValueChange = { rawInput ->
                            // 1. Remove all whitespace and newlines
                            val cleanedInput = rawInput.replace("\\s+".toRegex(), "")
                            rut = cleanedInput
                            // 2. Validar formato del RUT (ejemplo: 12345678-9)
                            rutValid = cleanedInput.matches("""^0*(\d{1,3}(\.?\d{3})*)-?([\dkK])$""".toRegex())
                        },
                        label = {
                            if (!rutHasFocus && rut.isEmpty()) { // Placeholder condicional
                                Text("Rut (12345678-9)")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(rutFocusRequester) // Asocia el FocusRequester
                            .onFocusChanged { rutHasFocus = it.hasFocus }, // Detecta el focus
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
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester), // Asocia el FocusRequester
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done, // "Done" en lugar de "Next"
                            keyboardType = KeyboardType.Password // Tipo password
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() } // Oculta el teclado
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Login Button
                    Button(
                        onClick = {
                            focusManager.clearFocus() // Cierra teclado antes de login
                            loginViewModel.login(rut, password)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf49600)),
                        enabled = uiState !is LoginViewModel.LoginUiState.Loading && rut.isNotEmpty() && rutValid
                    ) {
                        Text("Log In")
                    }

                    if (!rutValid) {
                        Text(
                            text = "Rut incorrecto",
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (uiState) {
                        is LoginViewModel.LoginUiState.Loading -> CircularProgressIndicator()
                        is LoginViewModel.LoginUiState.Error -> Text(
                            text = (uiState as LoginViewModel.LoginUiState.Error).message,
                            color = Color.Red
                        )
                        else -> {}
                    }
                }
            }
        }
    }
}
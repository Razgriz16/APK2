package com.example.oriencoop_score.navigation

/*Sealed class hace que todas aquellas clases dentro de esta, puedan heredar de Pantalla en este caso*/
//Básicamente acá se definen las rutas que luego serán llamadas por Navigation.kt

sealed class Pantalla (val route: String) {
    object PantallaPrincipal: Pantalla( "PantallaPrincipal")
    object Login: Pantalla( "Login")
    object MisProductos : Pantalla("MisProductos")
    object CuentaCap : Pantalla("CuentaCap")
    object CuentaAhorro : Pantalla("CuentaAhorro")
    object CreditoCuotas : Pantalla("CreditoCuotas")
    object Lcc : Pantalla("Lcc")
    }

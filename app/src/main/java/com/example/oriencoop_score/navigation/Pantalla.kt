package com.example.oriencoop_score.navigation

/*Sealed class hace que todas aquellas clases dentro de esta, puedan heredar de Pantalla en este caso*/
//Básicamente acá se definen las rutas que luego serán llamadas por Navigation.kt

sealed class Pantalla (val route: String) {
    object PantallaPrincipal: Pantalla( "PantallaPrincipal")
    object Login: Pantalla( "Login")
    object MisProductos : Pantalla("MisProductos")
    object CuentaCsocial : Pantalla("CuentaCsocial")
    object CuentaAhorro : Pantalla("CuentaAhorro")
    object CreditoCuotas : Pantalla("CreditoCuotas")
    object Lcc : Pantalla("Lcc")
    object ClienteInfo : Pantalla("ClienteInfo")
    object Notifications : Pantalla("Notifications")
    object Dap : Pantalla("Dap")
    object Lcr : Pantalla("Lcr")
    object Sucursales: Pantalla("Sucursales")
    }

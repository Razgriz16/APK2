package com.example.oriencoop_score.navigation

import com.example.oriencoop_score.view.pantalla_principal.PantallaPrincipal
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.oriencoop_score.view.Login
import com.example.oriencoop_score.view.mis_productos.MisProductos
import com.example.oriencoop_score.view.mis_productos.credito_cuotas.CreditoCuotas
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.CuentaAhorro
import com.example.oriencoop_score.view.mis_productos.cuenta_cap.CuentaCap
import com.example.oriencoop_score.view.mis_productos.dap.Dap
import com.example.oriencoop_score.view.mis_productos.lcc.Lcc
import com.example.oriencoop_score.view.mis_productos.lcr.Lcr
import com.example.oriencoop_score.view.pantalla_principal.ClienteInfo
import com.example.oriencoop_score.view.pantalla_principal.Sucursales.SucursalesScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Pantalla.Sucursales.route) {
        composable(route = Pantalla.Login.route) {
            Login(navController = navController)
        }

        composable(route = Pantalla.MisProductos.route) {
            MisProductos(navController = navController)
        }
        composable(route = Pantalla.PantallaPrincipal.route) {
            PantallaPrincipal(navController = navController)
        }

        composable(route = Pantalla.CuentaCap.route) {

            CuentaCap(navController = navController) /*, viewModel = harryPotterViewModel*/
        }

        composable(route = Pantalla.CuentaAhorro.route) {
            CuentaAhorro(navController = navController) /*, viewModel = harryPotterViewModel*/
        }

        composable(route = Pantalla.CreditoCuotas.route) {
            CreditoCuotas(navController = navController) /*, viewModel = harryPotterViewModel*/
        }

        composable(route = Pantalla.Lcc.route) {
            Lcc(navController = navController) /*, viewModel = harryPotterViewModel*/
        }

        composable(route = Pantalla.Lcr.route) {
            Lcr(navController = navController) /*, viewModel = harryPotterViewModel*/
        }

        composable(route = Pantalla.ClienteInfo.route) {
            ClienteInfo(navController = navController) /*, viewModel = harryPotterViewModel*/
        }

        composable(route = Pantalla.Dap.route) {
            Dap(navController = navController) /*, viewModel = harryPotterViewModel*/
        }

        composable(route = Pantalla.Sucursales.route) {
            SucursalesScreen(navController= navController) /*, viewModel = harryPotterViewModel*/
        }



    }
}



package com.example.oriencoop_score.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Sucursales(
    val admisionjuridica: String,
    val agente: String,
    val beta: String,
    val captacionesprime: String,
    val codigociudad: String,
    val codigocomuna: String,
    val codigodicom: String,
    val codigosucursal: String,
    val consumohora_lthr: String,
    val correo: String,
    val direccion: String,
    val ejecutivo_normalizacion: String,
    val eliminaadmisionrapida: String,
    val empresa: String,
    val estaactiva: String,
    val estado: String,
    val flag_generador: String,
    val grupo: String,
    val horario: String,
    val idplataforma: String,
    val localidad: String,
    val nombre: String,
    val nombreabreviado: String,
    val nuevoadmision: String,
    val plaza: String,
    val posteo: String,
    val potencia_generador: String,
    val potencia_utilizada: String,
    val recaudacionexterna: String,
    val region: String,
    val string_hermes: String,
    val tamano: String,
    val telefono: String,
    val validafechaaprobacion: String
)

data class ObtenerSucursalesResponse(
    val data: List<Sucursales>
)

data class Comunas(
    val activa: String,
    val aplica_plaft: String,
    val capital_provincia: String,
    val capital_region: String,
    val codigo_area: String,
    val codigo_cmf: String,
    val codigo_sii: String,
    val codigociudad: String,
    val codigocomuna: String,
    val codigofosis: String,
    val codigopostalcomuna: String,
    val codigosucursal: String,
    val idregion: String,
    val latitud: String,
    val longitud: String,
    val nombre: String,
    val nombre_region: String,
    val provincia: String,
    val region: String,
    val venta_web_directa: String
)

data class ObtenerComunasResponse(
    val data: List<Comunas>,
    val error_code: Double,
    val error_message: String
)




/*
data class Region(
    val name: String,
    val cities: List<City> = emptyList(),
    val isExpanded: MutableState<Boolean> = mutableStateOf(false)
)

data class City(
    val name: String,
    val branches: List<Branch> = emptyList(),
    val isExpanded: MutableState<Boolean> = mutableStateOf(false)
)
data class Branch(
    val name: String,
    val address: String,
    val phone: String,
    val agent: String,
    val schedule: List<String>,
    val latitude: Double,
    val longitude: Double,
    var isExpanded: Boolean = false
)
*/
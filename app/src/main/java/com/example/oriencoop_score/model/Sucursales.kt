package com.example.oriencoop_score.model
import com.google.gson.annotations.SerializedName

data class Sucursales(
    val agente: String?,
    val codigociudad: Int,
    val codigocomuna: Int,
    val codigosucursal: Int,
    val correo: String?,
    val direccion: String?,
    val estaactiva: String,
    val horario: String?,
    val nombre: String?,
    val region: String,
    val telefono: String?
)

data class SucursalesResponse(
    val data: List<Sucursales>
)



data class Ciudades(
    val activa: String,
    val codigociudad: Int,
    val nombre: String
)

data class CiudadesResponse(
    val data: List<Ciudades>,
    val error_code: Float,
    val error_message: String
)



data class Comunas(
    val activa: String?,
    val codigociudad: Int,
    val codigocomuna: Int,
    val codigosucursal: Int,
    val nombre: String?,
    val region: String
)

data class ComunasResponse(
    val data: List<Comunas>,
    val error_code: Float,
    val error_message: String
)



data class SucursalViewData(
    val nombre: String,
    val direccion: String,
    val horario: String,
    val telefono: String?, // Añadido por si es útil
    val correo: String?   // Añadido por si es útil
)

data class ComunaViewData( // Usaremos Comuna como nivel intermedio
    val nombreComuna: String,
    val sucursales: List<SucursalViewData>
)

data class RegionViewData(
    val nombreRegion: String,
    val comunas: List<ComunaViewData>
)
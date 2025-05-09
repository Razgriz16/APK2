package com.example.oriencoop_score.model
import com.google.gson.annotations.SerializedName

data class Sucursales(
    @SerializedName("agente") val agente: String?,
    @SerializedName("codigociudad") val codigociudad: Int,
    @SerializedName("codigocomuna") val codigocomuna: Int,
    @SerializedName("codigosucursal") val codigosucursal: Int,
    @SerializedName("correo") val correo: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("estaactiva") val estaactiva: String,
    @SerializedName("horario") val horario: String?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("region") val region: String,
    @SerializedName("telefono") val telefono: String?
)

data class SucursalesResponse(
    @SerializedName("data") val data: List<Sucursales>
)

data class Ciudades(
    @SerializedName("activa") val activa: String,
    @SerializedName("codigociudad") val codigociudad: Int,
    @SerializedName("nombre") val nombre: String
)

data class CiudadesResponse(
    @SerializedName("data") val data: List<Ciudades>,
    @SerializedName("error_code") val error_code: Float,
    @SerializedName("error_message") val error_message: String
)

data class Comunas(
    @SerializedName("activa") val activa: String?,
    @SerializedName("codigociudad") val codigociudad: Int,
    @SerializedName("codigocomuna") val codigocomuna: Int,
    @SerializedName("codigosucursal") val codigosucursal: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("region") val region: String
)

data class ComunasResponse(
    @SerializedName("data") val data: List<Comunas>,
    @SerializedName("error_code") val error_code: Float,
    @SerializedName("error_message") val error_message: String
)

data class SucursalViewData(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("horario") val horario: String,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("correo") val correo: String?
)

data class ComunaViewData(
    @SerializedName("nombreComuna") val nombreComuna: String,
    @SerializedName("sucursales") val sucursales: List<SucursalViewData>
)

data class RegionViewData(
    @SerializedName("nombreRegion") val nombreRegion: String,
    @SerializedName("comunas") val comunas: List<ComunaViewData>
)
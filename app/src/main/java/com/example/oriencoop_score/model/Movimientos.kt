package com.example.oriencoop_score.model

import com.google.gson.annotations.SerializedName

data class MovimientosCsocial(
    @SerializedName("cuenta") val cuenta: Long,
    @SerializedName("escargo") val escargo: String,
    @SerializedName("monto") val monto: Double,
    @SerializedName("nombre") val nombre:String,
    @SerializedName("nombreabreviado") val nombreabreviado:String,
    @SerializedName("sucursal") val sucursal:String,
    @SerializedName("fechapago") val fechapago: String
)

data class MovimientosCreditos(
    @SerializedName("fecha") val fecha: String,
    @SerializedName("fechahora") val fechaHora: String,
    @SerializedName("identificador") val identificador: Int,
    @SerializedName("montomovimiento") val montoMovimiento: Double,
    @SerializedName("rut") val rut: Int,
    @SerializedName("sucursal") val sucursal: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("usuario") val usuario: String
)

data class MovimientosAhorro(
    @SerializedName("correlativo") val correlativo: Int,
    @SerializedName("fechaefectiva") val fechaEfectiva: String,
    @SerializedName("identificador") val identificador: Int,
    @SerializedName("monto") val monto: Double,
    @SerializedName("nombre_transaccion") val nombreTransaccion: String,
    @SerializedName("sucursal") val sucursal: String,
    @SerializedName("sucursal_origen") val sucursalOrigen: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("usuario") val usuario: String
)

data class MovimientosLcc(
    val NROCUENTA: Long,
    val TIPOMOVIMIENTO: String,
    val FECHA: String,
    val SUCURSAL: String,
    val MONTO: String



)

data class MovimientosLccResponse(
    val movimientos_lcc: List<MovimientosLcc>
)



data class MovimientosLcr(
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("escargoabono") val escargoabono: String,
    @SerializedName("fechacontable") val fechacontable: String,
    @SerializedName("fechavencimiento") val fechavencimiento: String,
    @SerializedName("monto") val monto: Double,
    @SerializedName("nombremovimiento") val nombremovimiento: String,
    @SerializedName("numerodocumento") val numerodocumento: String
)


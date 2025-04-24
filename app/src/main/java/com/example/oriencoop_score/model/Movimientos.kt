package com.example.oriencoop_score.model

import com.google.gson.annotations.SerializedName

data class Movimiento(
    val CUENTA: Long,
    val FECHAPAGO: String,
    val MONTO: String,
    val NOMBREABRTRANSACCION: String,
    val NOMBRETRANSACCION: String,
    val ESCARGO: String
)

data class MovimientosResponse(
    val movimientos: List<Movimiento>
)



data class MovimientosAhorro(
    val MONTO: String,
    val FECHAMOV: String,
    val NOMBRETRANSAC: String,
    val NOMBRETRANSACABRV: String,
    val CARGOABONO: String,
    val NOMBRESUCURSAL: String,
    val NROCUENTA: Long


)

data class MovimientosAhorroResponse(
    val movimientos_ahorro: List<MovimientosAhorro>
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
    val DESCRIPCION: String,
    val ESCARGOABONO: String,
    val FECHACONTABLE: String,
    val FECHAVENCIMIENTO: String,
    val MONTO: String,
    val NOMBREMOVIMIENTO: String,
    val NUMERODOCUMENTO: String
)

data class MovimientosLcrResponse(
    val movimientos_lcr: List<MovimientosLcr>
)
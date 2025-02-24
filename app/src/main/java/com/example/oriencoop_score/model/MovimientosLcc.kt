package com.example.oriencoop_score.model

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
package com.example.oriencoop_score.model

data class MovimientosCreditos(
    val FECHA: String,
    val MONTO: String,
    val NOMBRE: String,
    val NROCUENTA: Long,
    val SUCURSAL: String
)

data class MovimientosCreditosResponse(
    val movimientos_creditos: List<MovimientosCreditos>
)
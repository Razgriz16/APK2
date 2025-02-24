package com.example.oriencoop_score.model

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
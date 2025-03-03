package com.example.oriencoop_score.model

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
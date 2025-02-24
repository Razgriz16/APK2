package com.example.oriencoop_score.model


data class CuentaCapResponse(
    val FECHAAPERTURA: String,
    val NROCUENTA: Long,
    val SALDOCONTABLE: String,
    val TIPOCUENTA: String
)

data class RutRequest(
    val rut: String
)

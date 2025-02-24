package com.example.oriencoop_score.model

data class CuentaAhorro(
    val TIPOCUENTA: String,
    val SALDODISPONIBLE: String,
    val SALDOCONTABLE: String,
    val SUCURSAL: String,
    val FECHAAPERTURA: String,
    val NROCUENTA: Long
)

data class CuentaAhorroResponse(
    val ahorro: List<CuentaAhorro>
)
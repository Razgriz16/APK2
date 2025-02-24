package com.example.oriencoop_score.model

data class CreditoCuota(
    val MONTOCREDITO: String,
    val NROCUENTA: Long,
    val NUMEROCUOTAS: Int,
    val PROXVENCIMIENTO: String,
    val TIPOCUENTA: String,
    val VALORCUOTA: String
)

data class CreditoCuotasResponse(
    val credito_cuotas: List<CreditoCuota>
)
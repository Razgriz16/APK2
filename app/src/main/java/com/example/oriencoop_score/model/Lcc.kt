package com.example.oriencoop_score.model

data class Lcc(
    val CUPOAUTORIZADO: String,
    val CUPOUTILIZADO: String,
    val CUPODISPONIBLE: String,
    val NROCUENTA: Long
)

data class LccResponse(
    val lcc: List<Lcc>
)
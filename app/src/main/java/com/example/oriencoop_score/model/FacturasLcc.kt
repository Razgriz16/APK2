package com.example.oriencoop_score.model

data class FacturasLcc(
    val DIASMORA: String,
    val ESTADO: String,
    val FECHAVENCIMIENTO: String,
    val MONTOFACTURADO: String
)

data class FacturasLccResponse(
    val facturas_lcc: List<FacturasLcc>
)
package com.example.oriencoop_score.model

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
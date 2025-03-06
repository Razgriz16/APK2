package com.example.oriencoop_score.model

import com.google.gson.annotations.SerializedName

data class Lcc(
    @SerializedName("CUPOAUTORIZADO") val cupoautorizado: String,
    @SerializedName("CUPOUTILIZADO") val cupoutilizado: String,
    @SerializedName("CUPODISPONIBLE") val cupodisponible: String,
    @SerializedName("NROCUENTA") val numerocuenta: Long
)

data class LccResponse(
    val lcc: List<Lcc>
)
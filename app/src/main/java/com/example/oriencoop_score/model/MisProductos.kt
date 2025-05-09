package com.example.oriencoop_score.model

import com.google.gson.annotations.SerializedName


data class CuentaCsocial(
    @SerializedName("cuenta") val cuenta: Long,
    @SerializedName("fechaapertura") val fechaapertura: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("saldocontable") val saldoContable: Double,
)



data class CuentaAhorro(
    @SerializedName("codigosistema") val codigoSistema: Int,
    @SerializedName("codigosucursal") val codigoSucursal: Int,
    @SerializedName("digitocuenta") val digitoCuenta: Int,
    //@SerializedName("estadocuenta") val estadoCuenta: Int,
    @SerializedName("nombre_producto") val nombreProducto: String,
    @SerializedName("nombre_producto_abreviado") val nombreProductoAbreviado: String,
    @SerializedName("numerocuenta") val numeroCuenta: Int,
    @SerializedName("fechaactivacion") val fechaActivacion: String? = null,
    @SerializedName("saldocontable") val saldoContable: Double,
    @SerializedName("saldodisponible") val saldoDisponible: Double,
    @SerializedName("trunc(cuentaahorro.fechaapertura)") val fechaApertura: String? = null
)



data class CreditoCuotas(
    @SerializedName("cedula") val cedula: Int,
    @SerializedName("credito") val credito: Long,
    @SerializedName("cuotas") val cuotas: Int,
    @SerializedName("desc_division") val desc_division: String,
    @SerializedName("desc_producto") val desc_producto: String,
    @SerializedName("diasmora") val diasmora: Int,
    @SerializedName("estado") val estado: Int,
    @SerializedName("estadocredito") val estadocredito: String,
    @SerializedName("fechaactivacion") val fechaactivacion: String,
    @SerializedName("fechavencimiento") val fechavencimiento: String,
    @SerializedName("fechacreacion") val fechacreacion: String,
    @SerializedName("fechaestado") val fechaestado: String,
    @SerializedName("montobruto_pesos") val montobruto_pesos: Int,
    @SerializedName("montoliquido_pesos") val montoliquido_pesos: Int,
    @SerializedName("numerocredito") val numerocredito: String,
    @SerializedName("numerocuotainicial") val numerocuotainicial: Int,
    @SerializedName("valorcuota_pesos") val valorcuota_pesos: Int,
    @SerializedName("valorcuotainicial") val valorcuotainicial: Double,
)



data class Dap(
    @SerializedName("codigo") val codigo: Long,
    @SerializedName("fechaActivacion") val fechaActivacion: String?,
    @SerializedName("fechaCreacion") val fechaCreacion: String?,
    @SerializedName("fechaLiquidacion") val fechaLiquidacion: String?,
    @SerializedName("fechaModificacion") val fechaModificacion: String?,
    @SerializedName("fechaRenovacion") val fechaRenovacion: String?,
    @SerializedName("fechaVencimiento") val fechaVencimiento: String?,
    @SerializedName("montointeres") val montoInteres: Double,
    @SerializedName("montoinversion") val montoInversion: Double,
    @SerializedName("nombre_estado") val nombreEstado: String,
    @SerializedName("nombre_producto") val nombreProducto: String,
    @SerializedName("numerodeposito") val numeroDeposito: String,
    @SerializedName("plazopactado") val plazoPactado: Int,
)



data class Lcc(
    @SerializedName("codigo") val codigo: Long,
    @SerializedName("cupootorgado") val cupoutilizado: Double,
    @SerializedName("cupodisponible") val cupodisponible: Double,
    @SerializedName("diasmora") val diasmora: Int,
    @SerializedName("ultimo_pago") val ultimo_pago: String,
)



data class Lcr(
    @SerializedName("cupoautorizado") val cupoautorizado: Double,
    @SerializedName("cupodisponible") val cupodisponible: Double,
    @SerializedName("cupoutilizado") val cupoutilizado: Double,
    @SerializedName("descripcionprod") val descripcionprod: String,
    @SerializedName("fechaativacion") val fechaativacion: String,
    @SerializedName("numerocuenta") val numerocuenta: String,
    @SerializedName("sucursal") val sucursal: String,
    @SerializedName("tipo") val tipo: String
)

package com.example.oriencoop_score.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val count: Int,
    val data: List<T>,
    val error_code: Int
)

data class CuentaCapResponse(
    val FECHAAPERTURA: String,
    val NROCUENTA: Long,
    val SALDOCONTABLE: String,
    val TIPOCUENTA: String
)

/*
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
*/

data class CuentaAhorro(
    @SerializedName("codigoagrupacionproducto") val codigoAgrupacionProducto: Int,
    @SerializedName("codigoproducto") val codigoProducto: Int,
    @SerializedName("codigosistema") val codigoSistema: Int,
    @SerializedName("codigosucursal") val codigoSucursal: Int,
    @SerializedName("digitocuenta") val digitoCuenta: Int,
    @SerializedName("estadocuenta") val estadoCuenta: Int,
    @SerializedName("montoantdevengint") val montoAntDevengInt: Double,
    @SerializedName("montoantdevengreaj") val montoAntDevengReaj: Double,
    @SerializedName("montoultdevengint") val montoUltDevengInt: Double,
    @SerializedName("montoultdevengreaj") val montoUltDevengReaj: Double,
    @SerializedName("nombre_estado") val nombreEstado: String,
    @SerializedName("nombre_producto") val nombreProducto: String,
    @SerializedName("nombre_producto_abreviado") val nombreProductoAbreviado: String,
    @SerializedName("numerocuenta") val numeroCuenta: Int,
    @SerializedName("numgirosefectuados") val numGirosEfectuados: Int,
    @SerializedName("fechaactivacion") val fechaActivacion: String? = null,
    @SerializedName("saldocontable") val saldoContable: Double,
    @SerializedName("saldodisponible") val saldoDisponible: Double,
    @SerializedName("fechaapertura") val fechaApertura: String
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


data class DapResponse(
    @SerializedName("CATEGORIA") val categoria: Int,
    @SerializedName("CODIGO") val codigo: Long,
    @SerializedName("DIRECCIONENVIO") val direccionEnvio: Int,
    @SerializedName("EJECUTIVO") val ejecutivo: Int,
    @SerializedName("EJECUTIVOVENTA") val ejecutivoVenta: Int,
    @SerializedName("ENCUSTODIA") val enCustodia: String,
    @SerializedName("ESTADO") val estado: Int,
    @SerializedName("FECHAACTIVACION") val fechaActivacion: String?,
    @SerializedName("FECHACREACION") val fechaCreacion: String?,
    @SerializedName("FECHALIQUIDACION") val fechaLiquidacion: String?,
    @SerializedName("FECHAMODIFICACION") val fechaModificacion: String?,
    @SerializedName("FECHARENOVACION") val fechaRenovacion: String?,
    @SerializedName("FECHAVENCIMIENTO") val fechaVencimiento: String?,
    @SerializedName("FOLIOPAGARE") val folioPagare: Int,
    @SerializedName("FORMAEMISION") val formaEmision: String,
    @SerializedName("IDDIVISION") val idDivision: Int,
    @SerializedName("IDPLATAFORMA") val idPlataforma: Int,
    @SerializedName("MONTOINTERES") val montoInteres: Double,
    @SerializedName("MONTOINTERESADICIONAL") val montoInteresAdicional: Double,
    @SerializedName("MONTOINTERESADICIONALPARIDAD") val montoInteresAdicionalParidad: Double,
    @SerializedName("MONTOINTERESPARIDAD") val montoInteresParidad: Double,
    @SerializedName("MONTOINVERSION") val montoInversion: Double,
    @SerializedName("MONTOINVERSIONPARIDAD") val montoInversionParidad: Double,
    @SerializedName("MONTOREAJUSTE") val montoReajuste: Double,
    @SerializedName("MONTOREAJUSTEADICIONAL") val montoReajusteAdicional: Double,
    @SerializedName("NOMBRE") val nombre: String,
    @SerializedName("NOMBRE_EJECUTIVO") val nombreEjecutivo: String,
    @SerializedName("NOMBRE_ESTADO") val nombreEstado: String,
    @SerializedName("NOMBRE_PRODUCTO") val nombreProducto: String,
    @SerializedName("NOMBRE_SUC_LIQUIDACION") val nombreSucLiquidacion: String?,
    @SerializedName("NOMBRE_SUC_ORIGEN") val nombreSucOrigen: String,
    @SerializedName("NROBENEFICIARIOS") val nroBeneficiarios: Int,
    @SerializedName("NROBLOQUEOSACTIVOS") val nroBloqueosActivos: Int,
    @SerializedName("NROPERIODOSPAGRPERIODICA") val nroPeriodosPagrPeriodica: Int?,
    @SerializedName("NROPERIODOSRPERIODICA") val nroPeriodosRPeriodica: Int?,
    @SerializedName("NRORENOVACIONES") val nroRenovaciones: Int,
    @SerializedName("NUMERODEPOSITO") val numeroDeposito: String,
    @SerializedName("PERIODICIDAD") val periodicidad: String?,
    @SerializedName("PERMITEGESTIONBLOQUEO") val permiteGestionBloqueo: String,
    @SerializedName("PERMITELIQUIDAR") val permiteLiquidar: String,
    @SerializedName("PERMITERETANT") val permiteRetant: String,
    @SerializedName("PLAZOACTUAL") val plazoActual: Int,
    @SerializedName("PLAZOPACTADO") val plazoPactado: Int,
    @SerializedName("PREMIOPORRENOVAR") val premioPorRenovar: Double,
    @SerializedName("PRODUCTO") val producto: Int,
    @SerializedName("RUT") val rut: String,
    @SerializedName("SEACOGEART57") val seAcogeArt57: String,
    @SerializedName("SESOLICITORETANT") val seSolicitoRetant: String,
    @SerializedName("SESOLICITOSOBRETASA") val seSolicitoSobretasa: String,
    @SerializedName("SUCURSALLIQUIDACION") val sucursalLiquidacion: Int?,
    @SerializedName("SUCURSALORIGEN") val sucursalOrigen: Int,
    @SerializedName("TASABASE") val tasaBase: Double,
    @SerializedName("TASAEQUIVALENTE") val tasaEquivalente: Double,
    @SerializedName("TIPOPERSONA") val tipoPersona: String,
    @SerializedName("USUARIOMODIFICACION") val usuarioModificacion: String?
)



data class Lcc(
    @SerializedName("CUPOAUTORIZADO") val cupoautorizado: String,
    @SerializedName("CUPOUTILIZADO") val cupoutilizado: String,
    @SerializedName("CUPODISPONIBLE") val cupodisponible: String,
    @SerializedName("NROCUENTA") val numerocuenta: Long
)

data class LccResponse(
    val lcc: List<Lcc>
)



data class LcrResponse(
    val CUPOAUTORIZADO: String,
    val CUPODISPONIBLE: String,
    val CUPOUTILIZADO: String,
    val DESCRIPCIONPROD: String,
    val FECHAATIVACION: String,
    val NUMEROCUENTA: String,
    val SUCURSAL: String,
    val TIPO: String
)

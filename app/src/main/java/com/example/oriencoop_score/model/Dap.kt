import com.google.gson.annotations.SerializedName

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




import java.security.MessageDigest
import java.nio.charset.Charset
import java.security.NoSuchAlgorithmException

fun getClaveSHA1(rut: String, str: String): String? {
    val primerHash = generarHash(str + rut)
    return generarHash(primerHash.toString())?.uppercase()
}

fun generarHash(text: String): String? {
    return try {
        val md = MessageDigest.getInstance("SHA-1")
        val sha1hash = md.digest(text.toByteArray(Charset.forName("ISO-8859-1")))

        //sha1hash.joinToString("") { String.format("%02x", it) }
        getHexadecimal(sha1hash)
    } catch (ex: NoSuchAlgorithmException) {
        println("Error: Algoritmo SHA-1 no encontrado.")
        null
    }
}

private fun getHexadecimal(data: ByteArray): String {
    val buf = StringBuilder()
    for (b in data) {
        var halfbyte = (b.toInt() ushr 4) and 0x0F // Conversión explícita a Int
        var two_halfs = 0
        do {
            buf.append(if (halfbyte in 0..9) {
                '0' + halfbyte
            } else {
                'a' + (halfbyte - 10)
            })
            halfbyte = b.toInt() and 0x0F // Conversión explícita a Int
        } while (two_halfs++ < 1)
    }
    return buf.toString()
}

fun cleanRut(rut: String): String {
    val regex = Regex("""^0*(\d{1,3}(\.?\d{3})*)-?([\dkK])$""")
    if (!rut.matches(regex)) {
        return rut // Or throw an exception if you prefer
    }
    val rutWithoutVerification = rut.substringBefore("-")

    return rutWithoutVerification.replace(".", "").replace("-", "") // Remove dots, dashes, and leading zeros
}

fun validRut(rut: String): Boolean {
    if (rut.matches(regex = """^0*(\d{1,3}(\.?\d{3})*)-?([\dkK])$""".toRegex())) {
        return true
    } else {
        return false
    }
}

fun main() {
    val rut = cleanRut("6.600.427-9")
    println(rut)
    val rutValido = validRut(rut)
    print("es valido:"+rutValido)
    val str = "1234"
    val claveSHA1 = getClaveSHA1(rut, str)
    println("Clave SHA-1 doble: $claveSHA1")
}

// 8A6ED74B51AAE3F12DF7A5E3B5D8D4B2F13671E7 (sin puntos ni guion)
// 86239E1B0725D5262713624B12DE7F86634A8B96

// B7EA670FE7067481CE7951F5D86B6A8E155BFEFB (6600427)

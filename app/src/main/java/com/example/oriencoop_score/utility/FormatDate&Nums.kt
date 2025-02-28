package com.example.oriencoop_score.utility

import java.text.NumberFormat
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

fun customDateFormat(dateString: String?): String? {
    if (dateString == null) return null

    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    return try {
        val date: Date? = inputFormat.parse(dateString) // Parse the input string into a Date object
        outputFormat.format(date!!) // Format the Date object into the desired output string
    } catch (e: Exception) {
        // Handle parsing exceptions. Log the error appropriately.
        println("Error parsing date: $dateString - ${e.localizedMessage}")
        null
    }
}

fun formatNumberWithDots2(number: Double): String {
    val formatter = NumberFormat.getInstance(Locale.US) // Or any locale you want
    return formatter.format(number).replace(",", ".")
}

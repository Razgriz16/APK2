package com.example.oriencoop_score.utility

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@SuppressLint("NewApi")
fun String?.toFormattedDate(): String? {
    if (this == null) return null

    val inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return try {
        LocalDateTime.parse(this, inputFormatter).format(outputFormatter)
    } catch (e: DateTimeParseException) {
        println("Error parsing date: $this") // Replace with proper logging
        null
    }
}
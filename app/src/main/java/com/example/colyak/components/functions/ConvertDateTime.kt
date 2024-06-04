package com.example.colyak.components.functions

import android.annotation.SuppressLint
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi")
fun convertDateTime(input: String): String {
    val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val dateTime = OffsetDateTime.parse(input, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH.mm", Locale.getDefault())
    return dateTime.withHour(dateTime.hour + 3).format(outputFormatter)
}
package com.cosmic.nytnews.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

object AppDateUtils {

    fun formatDateToReadableWords(dateString: String, locale: Locale = Locale.getDefault()): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatDateToReadableWordsApi26Plus(dateString, locale)
        } else {
            formatDateToReadableWordsBelowApi26(dateString, locale)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateToReadableWordsApi26Plus(dateString: String, locale: Locale): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            val date = LocalDate.parse(dateString, formatter)
            formatLocalDateToReadableWords(date, locale)
        } catch (e: DateTimeParseException) {
            // Handle parsing error (e.g., invalid date format)
            println("Error parsing date: $dateString. Returning original string.")
            dateString
        }
    }

    private fun formatDateToReadableWordsBelowApi26(dateString: String, locale: Locale): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", locale)
        return try {
            val date = formatter.parse(dateString) ?: return dateString
            formatDateToReadableWordsSimpleDateFormat(date, locale)
        } catch (e: Exception) {
            // Handle parsing error (e.g., invalid date format)
            println("Error parsing date: $dateString. Returning original string.")
            dateString
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatLocalDateToReadableWords(date: LocalDate, locale: Locale = Locale.getDefault()): String {
        val month = date.month.getDisplayName(TextStyle.FULL, locale)
        val day = date.dayOfMonth
        val year = date.year
        return "$month $day, $year"
    }

    private fun formatDateToReadableWordsSimpleDateFormat(date: Date, locale: Locale): String {
        val formatter = SimpleDateFormat("MMMM d, yyyy", locale)
        return formatter.format(date)
    }
}
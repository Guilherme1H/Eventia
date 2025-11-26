package com.example.eventia.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun isEventUpcoming(eventDateStr: String?): Boolean {
        if (eventDateStr.isNullOrEmpty()) {
            return false
        }

        return try {
            val formatter = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault())
            val eventDate = formatter.parse(eventDateStr)
            val currentDate = Date()

            eventDate?.after(currentDate) ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
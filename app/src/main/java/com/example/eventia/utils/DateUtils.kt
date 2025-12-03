package com.example.eventia.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val MYSQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

    private const val DISPLAY_FORMAT = "dd 'de' MMMM, HH:mm"

    fun formatarDataCompleta(mysqlDateTime: String): String {
        return try {
            val parser = SimpleDateFormat(MYSQL_DATETIME_FORMAT, Locale.getDefault())
            val formatter = SimpleDateFormat(DISPLAY_FORMAT, Locale("pt", "BR"))
            val date = parser.parse(mysqlDateTime)
            if (date != null) {
                formatter.format(date)
            } else {
                mysqlDateTime
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mysqlDateTime
        }
    }
    fun isEventUpcoming(mysqlDateTime: String): Boolean {
        return try {
            val parser = SimpleDateFormat(MYSQL_DATETIME_FORMAT, Locale.getDefault())
            val eventDate = parser.parse(mysqlDateTime)

            if (eventDate != null) {
                eventDate.after(Date())
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
package com.example.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object MeadowDateUtils {
    private fun isoFormat(): SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private fun friendlyFormat(): SimpleDateFormat =
        SimpleDateFormat("EEE, MMM d", Locale.US)

    private fun monthYearFormat(): SimpleDateFormat =
        SimpleDateFormat("MMMM yyyy", Locale.US)

    fun todayIso(): String = isoFormat().format(Date())

    fun dayOfYear(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.DAY_OF_YEAR) + cal.get(Calendar.YEAR) * 366
    }

    fun currentHour(): Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    fun formatFriendly(dateIso: String): String {
        val parsed = runCatching { isoFormat().parse(dateIso) }.getOrNull() ?: return dateIso
        return friendlyFormat().format(parsed)
    }

    fun monthOffsetIso(monthOffset: Int): Pair<Int, Int> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.MONTH, monthOffset)
        return cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH)
    }

    fun formatMonthYear(year: Int, monthZeroBased: Int): String {
        val cal = Calendar.getInstance()
        cal.set(year, monthZeroBased, 1)
        return monthYearFormat().format(cal.time)
    }

    fun daysInMonthGrid(year: Int, monthZeroBased: Int): List<String?> {
        val cal = Calendar.getInstance()
        cal.set(year, monthZeroBased, 1, 0, 0, 0)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY // 0..6
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val cells = mutableListOf<String?>()
        repeat(firstDayOfWeek) { cells.add(null) }
        for (day in 1..maxDay) {
            cal.set(Calendar.DAY_OF_MONTH, day)
            cells.add(isoFormat().format(cal.time))
        }
        while (cells.size % 7 != 0) {
            cells.add(null)
        }
        return cells
    }

    fun dayNumberFromIso(dateIso: String): Int {
        return dateIso.takeLast(2).toIntOrNull() ?: 1
    }
}

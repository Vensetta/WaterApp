package org.turter.water_app_mobile.presentation.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun LocalDate.toFormatDdMm(): String =
    "${dayOfMonth.addTrailingZeroIfLessTen()}.${monthNumber.addTrailingZeroIfLessTen()}"

fun LocalDateTime.toFormatTime(): String =
    "${time.hour.addTrailingZeroIfLessTen()}:${time.minute.addTrailingZeroIfLessTen()}"

private fun Int.addTrailingZeroIfLessTen(): String = if (this / 10 > 0) this.toString() else "0$this"
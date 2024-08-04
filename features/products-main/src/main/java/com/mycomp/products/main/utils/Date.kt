package com.mycomp.products.main.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = Date(millis)
    return formatter.format(date)
}
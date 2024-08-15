package com.eterationcase.app.feature.home.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by bedirhansaricayir on 15.08.2024
 */

fun String.parseDate(): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return formatter.parse(this) ?: Date()
}
package com.example.heal_go.util

import com.example.heal_go.data.network.response.DateDiff
import java.text.SimpleDateFormat
import java.util.*

private const val TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val MILLIS_ADD = 25_200_000

val timeStamp: String = SimpleDateFormat(TIME_FORMAT, Locale.US).format(System.currentTimeMillis())

fun dateDiff(oldDateString: String): DateDiff {
    val dateFormat = SimpleDateFormat(TIME_FORMAT, Locale.US)
    val oldDate = dateFormat.parse(oldDateString)
    oldDate.time = oldDate.time + MILLIS_ADD
    val curDate = dateFormat.parse(timeStamp)
    curDate.time = curDate.time + MILLIS_ADD

    val diff = curDate.time - oldDate.time

    return DateDiff(
        (diff / 1000),
        ((diff /1000) / 60),
        (((diff /1000) / 60) / 60),
        ((((diff /1000) / 60) / 60) / 24)
    )
}

package id.nerdstudio.newsreader.util

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.toRelativeTime(): CharSequence {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    format.timeZone = TimeZone.getTimeZone("GMT+7:00")
    val time = format.parse(this).time
    val now = System.currentTimeMillis()

    return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDayDate(): String {
    return SimpleDateFormat("EEEE, dd MMMM").format(Date())
}
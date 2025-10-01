package com.novacodestudios.grispisupport.presentation.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

fun formatMessageTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

fun formatMessageDate(timestamp: Long, context: Context): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = timestamp }

    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // Pazartesi, Salı vs.

    return when {
        isSameDay(now, date) -> context.getString(R.string.today)
        isYesterday(now, date) -> context.getString(R.string.yesterday)
        isSameWeek(now, date) -> dayFormat.format(date.time) // örn: "Perşembe"
        else -> dateFormat.format(date.time)                // örn: "27 Temmuz 2025"
    }
}

fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun isYesterday(now: Calendar, date: Calendar): Boolean {
    val yesterday = now.clone() as Calendar
    yesterday.add(Calendar.DAY_OF_YEAR, -1)
    return isSameDay(yesterday, date)
}

fun isSameWeek(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)
}

// yeni(sarı) açık(kırmızı) beklemede(mor) çözülmüş(yeşil) askıda(siyah)
//fun TicketStatus.toColor() = when (this) {
//    TicketStatus.OPEN -> Color(0xFFD32F2F) // kırmızı
//    TicketStatus.IN_PROGRESS -> Color(0xFFFFA000) // kaldırılacak
//    TicketStatus.ON_HOLD -> Color(0xFF1976D2)  // mor
//     TicketStatus.PENDING -> Color(0xFF000000) // siyah
//    TicketStatus.RESOLVED -> Color(0xFF9E9E9E) // yeşil
//}

fun TicketStatus.toColor() = when (this) {
    TicketStatus.NEW -> Color(0xFFFA8C16)      // Yumuşak sarı fa8c16
    TicketStatus.OPEN -> Color(0xFFFA541C)     // Pastel kırmızı fa541c
    TicketStatus.PENDING -> Color(0xFF722ed1)  // Mor (primary) 722ed1 622c91
    TicketStatus.RESOLVED -> Color(0xFF389e0d) // Doğal yeşil 389e0d
    TicketStatus.ON_HOLD -> Color(0xFF000000)  // Koyu gri / siyaha yakın
}

@Composable
fun TicketStatus.toUiName() = when (this) {
    TicketStatus.OPEN -> stringResource(R.string.status_open)
    //TicketStatus.IN_PROGRESS -> "Devam Ediyor"
    TicketStatus.PENDING -> stringResource(R.string.status_suspended)
    TicketStatus.ON_HOLD -> stringResource(R.string.status_on_hold)
    TicketStatus.RESOLVED -> stringResource(R.string.status_resolved)
    // TicketStatus.CLOSED -> "Kapalı"
    TicketStatus.NEW -> stringResource(R.string.status_new)
}
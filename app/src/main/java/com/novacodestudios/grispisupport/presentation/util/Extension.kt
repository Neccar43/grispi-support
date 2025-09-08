package com.novacodestudios.grispisupport.presentation.util

import androidx.compose.ui.graphics.Color
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

fun formatMessageDate(timestamp: Long): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = timestamp }

    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // Pazartesi, Salı vs.

    return when {
        isSameDay(now, date) -> "Bugün"
        isYesterday(now, date) -> "Dün"
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
    TicketStatus.NEW -> Color(0xFFE6D267)      // Yumuşak sarı
    TicketStatus.OPEN -> Color(0xFFD26767)     // Pastel kırmızı
    TicketStatus.PENDING -> Color(0xFF6650A4)  // Mor (primary)
    TicketStatus.RESOLVED -> Color(0xFF5A8C6D) // Doğal yeşil
    TicketStatus.ON_HOLD -> Color(0xFF333333)  // Koyu gri / siyaha yakın
}

fun TicketStatus.toUiName() = when (this) {
    TicketStatus.OPEN -> "Açık"
    //TicketStatus.IN_PROGRESS -> "Devam Ediyor"
      TicketStatus.PENDING -> "Askıda"
    TicketStatus.ON_HOLD -> "Beklemede"
    TicketStatus.RESOLVED -> "Çözüldü"
    // TicketStatus.CLOSED -> "Kapalı"
    TicketStatus.NEW -> "Yeni"
}
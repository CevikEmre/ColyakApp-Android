package com.example.colyak.components.functions
import java.util.Date

fun timeSince(date: Date): String {
    val seconds = ((Date().time - date.time) / 1000).toInt()
    var interval = seconds / 31536000

    if (interval >= 1) {
        return "$interval yıl önce"
    }
    interval = seconds / 2592000
    if (interval >= 1) {
        return "$interval ay önce"
    }
    interval = seconds / 86400
    if (interval >= 1) {
        return "$interval gün önce"
    }
    interval = seconds / 3600
    if (interval >= 1) {
        return "$interval saat önce"
    }
    interval = seconds / 60
    if (interval >= 1) {
        return "$interval dakika önce"
    }
    return "$seconds saniye önce"
}
package com.resonix.uidemo.ui.util

import java.util.Locale

/** Formats a millisecond duration as "m:ss" (e.g. 3:05), for UI display only. */
fun formatMillis(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%d:%02d", minutes, seconds)
}

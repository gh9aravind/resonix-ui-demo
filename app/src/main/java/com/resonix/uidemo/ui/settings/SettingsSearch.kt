package com.resonix.uidemo.ui.settings

/**
 * A minimal case-insensitive match used to filter Settings rows as the
 * person types in the screen's search field. A blank query matches
 * everything, so every row is visible by default.
 */
fun settingsQueryMatches(query: String, vararg searchableText: String): Boolean {
    val trimmed = query.trim()
    if (trimmed.isEmpty()) return true
    return searchableText.any { it.contains(trimmed, ignoreCase = true) }
}

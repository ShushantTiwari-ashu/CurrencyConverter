package dev.shushant.dashboard.utils

fun getFlagEmoji(currencyCode: String): String {
    val countryCode = currencyCode.substring(0, 2).uppercase()
    val flagOffset = 0x1F1E6
    val asciiOffset = 0x41

    val firstChar = Character.codePointAt(countryCode, 0) - asciiOffset + flagOffset
    val secondChar = Character.codePointAt(countryCode, 1) - asciiOffset + flagOffset

    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}
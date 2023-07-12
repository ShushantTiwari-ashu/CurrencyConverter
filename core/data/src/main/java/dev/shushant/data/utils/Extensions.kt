package dev.shushant.data.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

fun Instant.isTimestamp30MinutesAhead(): Boolean {
    val currentTimestamp = Clock.System.now() //30
    val givenTimestamp = this.plus(30.minutes)// 39
    return givenTimestamp <= currentTimestamp
}
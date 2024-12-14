package com.github.lucascalheiros.common.datetime

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun Long.millisToLocalDateTime(): LocalDateTime {
    return LocalDateTime.from(
        Instant.ofEpochMilli(this).atZone(
            ZoneId.systemDefault()
        )
    )
}

fun Long.secondsToLocalDateTime(): LocalDateTime {
    return LocalDateTime.from(
        Instant.ofEpochSecond(this).atZone(
            ZoneId.systemDefault()
        )
    )
}

fun LocalDateTime.toMillis(): Long {
    return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.millisToLocalDate(): LocalDate {
    return LocalDate.from(
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    )
}
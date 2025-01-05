package com.github.lucascalheiros.common.strings

import java.text.Normalizer

fun String.normalizeString(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("[^\\p{ASCII}]".toRegex(), "")
}
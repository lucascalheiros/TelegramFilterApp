package com.github.lucascalheiros.data.frameworks.telegram

import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("UNCHECKED_CAST")
suspend fun <T : TdApi.Object> Client.send(query: TdApi.Function<T>): T =
    suspendCoroutine { continuation ->
        send(
            query,
            {
                if (it is TdApi.Error) {
                    continuation.resumeWithException(TdLibError(query, it))
                } else {
                    continuation.resume(it as T)
                }
            },
            {
                continuation.resumeWithException(it)
            }
        )
    }

class TdLibError(query: TdApi.Function<*>, error: TdApi.Error): Exception("$query\n$error")

fun TdApi.MessageContent.textContent(): String {
    return when (this) {
        is TdApi.MessageText -> {
            text.text
        }

        is TdApi.MessagePhoto -> {
            caption.text
        }

        else -> {
            toString()
        }
    }
}
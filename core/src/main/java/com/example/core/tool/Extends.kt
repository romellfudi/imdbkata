package com.example.core.tool

import kotlinx.coroutines.flow.MutableSharedFlow
import java.security.MessageDigest

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
// Computes the SHA-256 hash of this string and returns it as a hexadecimal string.
fun String.sha256(): String {
    val bytes = toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.joinToString("") {
        String.format("%02x", it)
    }
}

// Calls the given block if all elements in this collection are not null.
fun <T : Any, R : Any> Collection<T?>.whenAllNotNull(block: (List<T>) -> R) {
    if (this.all { it != null }) {
        block(this.filterNotNull()) // or do unsafe cast to non null collection
    }
}

// Calls the given block if any element in this collection is not null.
fun <T : Any, R : Any> Collection<T?>.whenAnyNotNull(block: (List<T>) -> R) {
    if (this.any { it != null }) {
        block(this.filterNotNull())
    }
}

// Creates a new MutableSharedFlow with zero replay and one extra buffer capacity.
fun <T> mutableNotReplayFlow(): MutableSharedFlow<T> {
    return MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1
    )
}
package com.onecall.data

data class CodeLease(
    val code: String,
    val generatedAtMillis: Long,
    val validityMillis: Long = DEFAULT_VALIDITY_MILLIS,
) {
    val expiresAtMillis: Long
        get() = generatedAtMillis + validityMillis

    fun isValid(nowMillis: Long = System.currentTimeMillis()): Boolean {
        return nowMillis < expiresAtMillis
    }

    companion object {
        const val DEFAULT_VALIDITY_MILLIS = 10 * 60 * 1000L
    }
}
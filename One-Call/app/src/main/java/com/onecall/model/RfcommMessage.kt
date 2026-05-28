package com.onecall.model

data class RfcommMessage(
    val type: String,
    val number: String? = null,
    val name: String? = null,
    val targetNumber: String? = null,
    val callerName: String? = null,
    val callerNumber: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

object MessageType {
    const val RING_START = "RING_START"
    const val RING_STOP = "RING_STOP"
    const val CALL_ACCEPTED_MAIN = "CALL_ACCEPTED_MAIN"
    const val CALL_ACCEPTED_SECONDARY = "CALL_ACCEPTED_SECONDARY"
    const val CALL_ENDED = "CALL_ENDED"
    const val TRANSFER_REQUEST = "TRANSFER_REQUEST"
    const val TRANSFER_ACCEPTED = "TRANSFER_ACCEPTED"
    const val TRANSFER_REJECTED = "TRANSFER_REJECTED"
    const val OUTGOING_REQUEST = "OUTGOING_REQUEST"
    const val OUTGOING_ALLOWED = "OUTGOING_ALLOWED"
    const val OUTGOING_BLOCKED = "OUTGOING_BLOCKED"
    const val HISTORY_SYNC = "HISTORY_SYNC"
    const val KEEP_ALIVE = "KEEP_ALIVE"
}

package com.onecall.core

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import com.onecall.model.MessageType
import com.onecall.model.RfcommMessage

class BluetoothCallBridge(
    private val context: Context,
    private val rfcomm: RfcommSignalingService
) {
    companion object {
        private const val TAG = "BluetoothCallBridge"
    }

    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    private var currentRingingNumber: String? = null
    private var isListening = false

    @Suppress("DEPRECATION")
    private val callStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            Log.d(TAG, "Call state: $state, number: $phoneNumber")
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    currentRingingNumber = phoneNumber
                    val name = resolveContactName(phoneNumber)
                    rfcomm.sendMessage(
                        RfcommMessage(
                            type = MessageType.RING_START,
                            number = phoneNumber,
                            name = name,
                            callerName = name,
                            callerNumber = phoneNumber
                        )
                    )
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    rfcomm.sendMessage(RfcommMessage(type = MessageType.CALL_ACCEPTED_MAIN))
                }
                TelephonyManager.CALL_STATE_IDLE -> {
                    rfcomm.sendMessage(RfcommMessage(type = MessageType.CALL_ENDED))
                    currentRingingNumber = null
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    fun startListening() {
        if (isListening) return
        try {
            telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE)
            isListening = true
            Log.d(TAG, "Started listening for call state changes")
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException starting call state listener", e)
        }
    }

    @Suppress("DEPRECATION")
    fun stopListening() {
        if (!isListening) return
        try {
            telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_NONE)
            isListening = false
        } catch (e: Exception) {
            Log.w(TAG, "Error stopping call state listener", e)
        }
    }

    private fun resolveContactName(number: String?): String? {
        if (number == null) return null
        return try {
            val uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number)
            )
            val cursor: Cursor? = context.contentResolver.query(
                uri,
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
                null, null, null
            )
            cursor?.use {
                if (it.moveToFirst()) it.getString(0) else null
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "No permission to read contacts")
            null
        } catch (e: Exception) {
            Log.w(TAG, "Error resolving contact name", e)
            null
        }
    }

    fun getCurrentRingingNumber(): String? = currentRingingNumber
}

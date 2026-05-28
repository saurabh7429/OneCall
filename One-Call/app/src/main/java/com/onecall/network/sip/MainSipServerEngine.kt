package com.onecall.network.sip

import android.content.Context
import android.telecom.TelecomManager
import android.util.Log
import androidx.core.content.getSystemService
import com.onecall.data.ConnectedDevice
import com.onecall.utils.getLocalIpAddress
import local.server.Proxy
import local.server.ServerProfile
import org.zoolu.sip.address.NameAddress
import org.zoolu.sip.call.Call
import org.zoolu.sip.call.CallListener
import org.zoolu.sip.message.Message
import org.zoolu.sip.message.MessageFactory
import org.zoolu.sip.message.SipResponses
import org.zoolu.sip.provider.SipProvider
import java.util.Enumeration
import java.util.Vector
import java.util.concurrent.ConcurrentHashMap

class MainSipServerEngine(
    private val context: Context,
) {
    private var sipProvider: SipProvider? = null
    private var proxy: OneCallSipProxy? = null
    private var localIp: String? = null
    private var activeCalls = ConcurrentHashMap<String, Call>()
    private var callAccepted = false
    
    // Hold pending outgoing requests
    private val pendingOutgoingRequests = ConcurrentHashMap<String, Message>()

    fun start(): Boolean {
        val ip = context.getLocalIpAddress() ?: return false
        localIp = ip

        if (sipProvider != null) {
            return true
        }

        val provider = SipProvider(ip, SIP_PORT)
        val profile = ServerProfile(null).apply {
            domain_names = arrayOf(ip)
            is_registrar = true
            is_open_proxy = true
            do_authentication = false
            do_proxy_authentication = false
            location_db = context.filesDir.resolve("onecall_sip_users.db").absolutePath
        }

        val serverProxy = OneCallSipProxy(provider, profile)
        sipProvider = provider
        proxy = serverProxy
        return true
    }

    fun stop() {
        activeCalls.values.forEach { call ->
            runCatching { call.cancel() }
        }
        activeCalls.clear()
        proxy = null
        sipProvider?.halt()
        sipProvider = null
    }

    fun broadcastIncomingCall(phoneNumber: String?) {
        if (sipProvider == null) {
            return
        }

        Log.i(TAG, "INCOMING CALL DETECTED: ${phoneNumber ?: "unknown"}")

        val registeredContacts = proxy?.getRegisteredContactUrls().orEmpty()
        if (registeredContacts.isEmpty()) {
            return
        }

        callAccepted = false
        val callerAddress = buildCallerSipAddress(phoneNumber)
        registeredContacts.forEach { contactUrl ->
            val call = Call(
                sipProvider,
                buildMainSipAddress(),
                buildMainSipContact(),
                object : CallListener {
                    override fun onCallIncoming(call: Call, callee: NameAddress, caller: NameAddress, sdp: String?, invite: Message) {
                        // no-op
                    }

                    override fun onCallModifying(call: Call, sdp: String?, invite: Message) = Unit

                    override fun onCallRinging(call: Call, resp: Message) = Unit

                    override fun onCallAccepted(call: Call, sdp: String?, resp: Message) {
                        if (callAccepted) return
                        callAccepted = true
                        cancelOtherCalls(call)
                        startRtpBridge()
                    }

                    override fun onCallRefused(call: Call, reason: String?, resp: Message) {
                        if (callAccepted) return
                        callAccepted = true
                        cancelOtherCalls(call)
                        rejectRealCall()
                    }

                    override fun onCallRedirection(call: Call, reason: String?, contactList: Vector<String>?, resp: Message) {
                        // no-op
                    }

                    override fun onCallConfirmed(call: Call, sdp: String?, ack: Message) = Unit

                    override fun onCallTimeout(call: Call) {
                        if (callAccepted) return
                        cancelOtherCalls(call)
                    }

                    override fun onCallReInviteAccepted(call: Call, sdp: String?, resp: Message) = Unit

                    override fun onCallReInviteRefused(call: Call, reason: String?, resp: Message) = Unit

                    override fun onCallReInviteTimeout(call: Call) = Unit

                    override fun onCallCanceling(call: Call, cancel: Message) = Unit

                    override fun onCallClosing(call: Call, bye: Message) = Unit

                    override fun onCallClosed(call: Call, resp: Message) {
                        activeCalls.remove(contactUrl)
                        stopRtpBridge()
                    }
                },
            )

            activeCalls[contactUrl] = call
            call.call(contactUrl, callerAddress, buildMainSipContact(), null, null)
        }
    }

    fun getRegisteredSecondaryCount(): Int {
        return proxy?.getRegisteredContactUrls()?.size ?: 0
    }

    fun updateConnectedDevices(devices: List<ConnectedDevice>) {
        // No-op for now; reserved for SIP registration analytics.
    }

    private fun cancelOtherCalls(acceptedCall: Call) {
        activeCalls.entries.forEach { (target, call) ->
            if (call !== acceptedCall) {
                runCatching { call.cancel() }
            }
        }
        activeCalls.clear()
    }

    private fun startRtpBridge() {
        Log.i(TAG, "RTP bridge start requested")
    }

    private fun stopRtpBridge() {
        Log.i(TAG, "RTP bridge stop requested")
    }

    private fun rejectRealCall() {
        val telecomManager = context.getSystemService<TelecomManager>() ?: return
        runCatching {
            val method = TelecomManager::class.java.getMethod("endCall")
            method.invoke(telecomManager)
        }
    }

    private fun buildMainSipAddress(): String {
        val ip = localIp ?: "0.0.0.0"
        return "sip:main@$ip"
    }

    private fun buildMainSipContact(): String {
        val ip = localIp ?: "0.0.0.0"
        return "sip:main@$ip:$SIP_PORT"
    }

    private fun buildCallerSipAddress(phoneNumber: String?): String {
        val ip = localIp ?: "0.0.0.0"
        val user = phoneNumber?.filter { it.isDigit() }.orEmpty().ifBlank { "main" }
        return "sip:$user@$ip"
    }

    private inner class OneCallSipProxy(provider: SipProvider, profile: ServerProfile) : Proxy(provider, profile) {
        fun getRegisteredContactUrls(): List<String> {
            val contacts = mutableListOf<String>()
            val users: Enumeration<*> = location_service.getUsers()
            while (users.hasMoreElements()) {
                val user = users.nextElement() as? String ?: continue
                val contactUrls = location_service.getUserContactURLs(user)
                while (contactUrls.hasMoreElements()) {
                    val contact = contactUrls.nextElement() as? String ?: continue
                    contacts.add(contact)
                }
            }
            return contacts.distinct()
        }

        override fun processRequestToLocalUser(msg: Message) {
            val targetUser = msg.requestLine.address.userName
            if (msg.isInvite() && !targetUser.isNullOrBlank() && targetUser != "main") {
                if (targetUser.all { it.isDigit() }) {
                    val deviceName = msg.fromHeader.nameAddress.address?.userName ?: "Secondary Device"
                    
                    // Send 100 Trying while waiting for user approval
                    val trying = MessageFactory.createResponse(msg, 100, SipResponses.reasonOf(100), null)
                    sip_provider.sendMessage(trying)
                    
                    pendingOutgoingRequests[targetUser] = msg
                    
                    val intent = android.content.Intent("com.onecall.OUTGOING_CALL_REQUEST").apply {
                        putExtra("number", targetUser)
                        putExtra("deviceName", deviceName)
                        setPackage(context.packageName)
                    }
                    context.sendBroadcast(intent)
                    return
                }
            }

            super.processRequestToLocalUser(msg)
        }
    }

    fun approvePendingOutgoingCall(number: String) {
        val msg = pendingOutgoingRequests.remove(number) ?: return
        val response = MessageFactory.createResponse(msg, 200, SipResponses.reasonOf(200), null)
        sipProvider?.sendMessage(response)
        
        val intent = android.content.Intent(android.content.Intent.ACTION_CALL).apply {
            data = android.net.Uri.parse("tel:$number")
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(intent) }
    }

    fun rejectPendingOutgoingCall(number: String) {
        val msg = pendingOutgoingRequests.remove(number) ?: return
        val response = MessageFactory.createResponse(msg, 403, SipResponses.reasonOf(403), null)
        sipProvider?.sendMessage(response)
    }

    companion object {
        private const val TAG = "OneCallSipServer"
        private const val SIP_PORT = 5060
    }
}

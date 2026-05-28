package com.onecall.network.sip

import android.content.Context
import android.util.Log
import com.onecall.data.SecondaryConnectionRecord
import com.onecall.data.DeviceRoleStore
import com.onecall.network.socket.OneCallConnectionManager
import com.onecall.ui.calls.CallActiveActivity
import com.onecall.ui.calls.IncomingCallActivity
import com.onecall.utils.getLocalIpAddress
import local.ua.UserAgent
import local.ua.UserAgentListener
import local.ua.UserAgentProfile
import org.zoolu.sip.address.NameAddress
import org.zoolu.sip.provider.RegisterAgent
import org.zoolu.sip.provider.RegisterAgentListener
import org.zoolu.sip.provider.SipProvider

object SecondarySipClient : RegisterAgentListener, UserAgentListener {
    private var sipProvider: SipProvider? = null
    private var registerAgent: RegisterAgent? = null
    private var userAgent: UserAgent? = null
    private var appContext: Context? = null

    fun start(context: Context, record: SecondaryConnectionRecord) {
        stop()
        appContext = context.applicationContext

        val localIp = context.getLocalIpAddress() ?: return
        val deviceId = record.secondaryDeviceId
        val sipPort = SIP_CLIENT_PORT

        val provider = SipProvider(localIp, sipPort)
        val profile = UserAgentProfile().apply {
            username = deviceId
            contact_url = "sip:$deviceId@$localIp:$sipPort"
            from_url = "sip:$deviceId@${record.mainHost}"
            audio = true
            no_prompt = true
        }

        val agent = RegisterAgent(
            provider,
            "sip:$deviceId@${record.mainHost}",
            profile.contact_url,
            this,
        )

        val ua = UserAgent(provider, profile, this)
        ua.listen()

        sipProvider = provider
        registerAgent = agent
        userAgent = ua

        agent.loopRegister(3600, 1800)
    }

    fun stop() {
        registerAgent?.halt()
        registerAgent = null
        userAgent?.hangup()
        userAgent = null
        sipProvider?.halt()
        sipProvider = null
        appContext = null
    }

    fun acceptIncomingCall() {
        userAgent?.accept()
    }

    fun declineIncomingCall() {
        userAgent?.hangup()
    }

    fun placeOutgoingCall(number: String, mainHost: String) {
        val targetUrl = "sip:$number@$mainHost"
        userAgent?.call(targetUrl)
    }

    override fun onUaRegistrationSuccess(ra: RegisterAgent, target: NameAddress, contact: NameAddress, result: String) {
        Log.i(TAG, "Secondary SIP registered: $result")
    }

    override fun onUaRegistrationFailure(ra: RegisterAgent, target: NameAddress, contact: NameAddress, result: String) {
        Log.w(TAG, "Secondary SIP registration failed: $result")
    }

    override fun onUaCallIncoming(ua: UserAgent, callee: NameAddress, caller: NameAddress) {
        Log.i(TAG, "Secondary SIP incoming call from ${caller.toString()}")
        val context = appContext ?: return
        val callerUser = caller.address?.userName
        val phoneNumber = callerUser?.takeIf { it.any(Char::isDigit) }
        val deviceCount = OneCallConnectionManager.lastKnownDeviceCount()
        IncomingCallActivity.startIncoming(
            context = context,
            role = DeviceRoleStore.ROLE_SECONDARY,
            phoneNumber = phoneNumber,
            ringingCount = deviceCount,
        )
    }

    override fun onUaCallCancelled(ua: UserAgent) {
        Log.i(TAG, "Secondary SIP call cancelled")
        appContext?.let {
            IncomingCallActivity.sendFinish(it)
            CallActiveActivity.sendFinish(it)
        }
    }

    override fun onUaCallRinging(ua: UserAgent) {
        Log.i(TAG, "Secondary SIP ringing")
    }

    override fun onUaCallAccepted(ua: UserAgent) {
        Log.i(TAG, "Secondary SIP call accepted")
    }

    override fun onUaCallTrasferred(ua: UserAgent) = Unit

    override fun onUaCallFailed(ua: UserAgent) {
        Log.w(TAG, "Secondary SIP call failed")
        appContext?.let {
            IncomingCallActivity.sendFinish(it)
            CallActiveActivity.sendFinish(it)
        }
    }

    override fun onUaCallClosed(ua: UserAgent) {
        Log.i(TAG, "Secondary SIP call closed")
        appContext?.let {
            IncomingCallActivity.sendFinish(it)
            CallActiveActivity.sendFinish(it)
        }
    }

    private const val TAG = "OneCallSipClient"
    private const val SIP_CLIENT_PORT = 5072
}

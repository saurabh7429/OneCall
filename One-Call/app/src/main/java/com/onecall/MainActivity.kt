package com.onecall

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.onecall.data.DeviceRoleStore
import com.onecall.network.sip.SecondarySipClient
import com.onecall.network.socket.OneCallConnectionManager
import com.onecall.service.SipServerService
import com.onecall.ui.calls.CallActiveActivity

class MainActivity : AppCompatActivity() {

    private val outgoingCallReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val number = intent.getStringExtra("number") ?: return
            val deviceName = intent.getStringExtra("deviceName") ?: "Secondary Device"
            
            val autoApprove = getSharedPreferences("onecall_prefs", Context.MODE_PRIVATE)
                .getBoolean("auto_approve_calls", false)
                
            if (autoApprove) {
                SipServerService.approveCall(this@MainActivity, number)
            } else {
                showApprovalDialog(number, deviceName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SipServerService.start(this)
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("com.onecall.OUTGOING_CALL_REQUEST")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(outgoingCallReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(outgoingCallReceiver, filter)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(outgoingCallReceiver)
    }

    private fun showApprovalDialog(number: String, deviceName: String) {
        AlertDialog.Builder(this)
            .setTitle("Outgoing Call Request")
            .setMessage("$deviceName call karna chahta hai: $number")
            .setPositiveButton("Allow") { _, _ ->
                SipServerService.approveCall(this, number)
            }
            .setNegativeButton("Block") { _, _ ->
                SipServerService.rejectCall(this, number)
            }
            .setCancelable(false)
            .show()
    }

    fun initiateOutgoingCall(number: String, contactName: String) {
        if (DeviceRoleStore.isMain(this)) {
            // Main device places actual call directly
            placeActualCall(number)
        } else {
            // Secondary device delegates to Main via SIP
            val record = OneCallConnectionManager.getLastSecondaryConnectionRecord(this)
            if (record != null) {
                SecondarySipClient.placeOutgoingCall(number, record.mainHost)
                Toast.makeText(this, "Requesting call via main device...", Toast.LENGTH_SHORT).show()
                // Transition to Active Call Activity
                CallActiveActivity.startOutgoing(this, number, contactName)
            } else {
                Toast.makeText(this, "Not connected to main device", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun placeActualCall(number: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
            CallActiveActivity.startOutgoing(this, number, "Contact")
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 101)
        }
    }
}

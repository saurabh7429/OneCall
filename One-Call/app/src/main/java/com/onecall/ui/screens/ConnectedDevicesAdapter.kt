package com.onecall.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.onecall.R
import com.onecall.data.ConnectedDevice
import com.onecall.data.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectedDevicesAdapter(
    private val onSettingsClicked: (String) -> Unit,
    private val onStopCallsClicked: (String, Boolean) -> Unit,
    private val onDisconnectClicked: (String) -> Unit
) : ListAdapter<ConnectedDevice, ConnectedDevicesAdapter.ViewHolder>(DeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_connected_device, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_device_icon)
        private val tvName: TextView = itemView.findViewById(R.id.tv_device_name)
        private val tvPaused: TextView = itemView.findViewById(R.id.tv_calls_paused)
        private val btnStopCalls: MaterialButton = itemView.findViewById(R.id.btn_stop_calls)
        private val btnDisconnect: MaterialButton = itemView.findViewById(R.id.btn_disconnect)

        fun bind(device: ConnectedDevice) {
            val settingsRepo = SettingsRepository.getInstance(itemView.context)
            
            // Allow tapping anywhere on card to go to settings
            itemView.setOnClickListener { onSettingsClicked(device.deviceId) }
            
            btnDisconnect.setOnClickListener { onDisconnectClicked(device.deviceId) }

            CoroutineScope(Dispatchers.IO).launch {
                val config = settingsRepo.getDeviceConfig(device.deviceId)
                
                withContext(Dispatchers.Main) {
                    tvName.text = config.nickname
                    
                    val iconRes = when (config.deviceIcon) {
                        "TABLET" -> android.R.drawable.ic_menu_crop // Placeholder
                        "FRIDGE" -> android.R.drawable.ic_menu_crop
                        "TV" -> android.R.drawable.ic_menu_crop
                        else -> android.R.drawable.ic_menu_call
                    }
                    ivIcon.setImageResource(iconRes)
                    
                    if (config.isCallsPaused) {
                        tvPaused.visibility = View.VISIBLE
                        btnStopCalls.text = "Resume Calls"
                        btnStopCalls.setTextColor(itemView.context.getColor(R.color.onecall_success))
                        btnStopCalls.strokeColor = itemView.context.getColorStateList(R.color.onecall_success)
                    } else {
                        tvPaused.visibility = View.GONE
                        btnStopCalls.text = "Stop Calls"
                        btnStopCalls.setTextColor(itemView.context.getColor(R.color.onecall_error))
                        btnStopCalls.strokeColor = itemView.context.getColorStateList(R.color.onecall_error)
                    }
                    
                    btnStopCalls.setOnClickListener { 
                        onStopCallsClicked(device.deviceId, config.isCallsPaused) 
                        // Update UI optimistically
                        btnStopCalls.isEnabled = false
                        it.postDelayed({ btnStopCalls.isEnabled = true }, 500)
                    }
                }
            }
        }
    }

    class DeviceDiffCallback : DiffUtil.ItemCallback<ConnectedDevice>() {
        override fun areItemsTheSame(oldItem: ConnectedDevice, newItem: ConnectedDevice): Boolean {
            return oldItem.deviceId == newItem.deviceId
        }

        override fun areContentsTheSame(oldItem: ConnectedDevice, newItem: ConnectedDevice): Boolean {
            return oldItem == newItem
        }
    }
}

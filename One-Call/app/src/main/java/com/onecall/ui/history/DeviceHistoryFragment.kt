package com.onecall.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onecall.R
import com.onecall.data.history.DeviceHistoryEntity
import com.onecall.data.history.HistoryRepository
import com.onecall.network.socket.OneCallConnectionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DeviceHistoryFragment : Fragment(R.layout.fragment_device_history) {

    private lateinit var rvDeviceHistory: RecyclerView
    private lateinit var tvEmptyDevices: TextView
    private lateinit var repository: HistoryRepository

    private val adapter = DeviceHistoryAdapter(
        onRemoveClick = { entry ->
            showRemoveDialog(entry)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        repository = HistoryRepository.getInstance(requireContext())
        
        rvDeviceHistory = view.findViewById(R.id.rv_device_history)
        tvEmptyDevices = view.findViewById(R.id.tv_empty_devices)

        rvDeviceHistory.layoutManager = LinearLayoutManager(requireContext())
        rvDeviceHistory.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repository.getDeviceHistory().collectLatest { list ->
                adapter.submitList(list)
                if (list.isEmpty()) {
                    rvDeviceHistory.visibility = View.GONE
                    tvEmptyDevices.visibility = View.VISIBLE
                } else {
                    rvDeviceHistory.visibility = View.VISIBLE
                    tvEmptyDevices.visibility = View.GONE
                }
            }
        }
    }

    private fun showRemoveDialog(entry: DeviceHistoryEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove Device")
            .setMessage("Permanently remove ${entry.deviceName} from history?")
            .setPositiveButton("Remove") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.deleteDeviceHistory(entry.deviceId)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

class DeviceHistoryAdapter(
    private val onRemoveClick: (DeviceHistoryEntity) -> Unit
) : RecyclerView.Adapter<DeviceHistoryAdapter.ViewHolder>() {

    private var list = listOf<DeviceHistoryEntity>()

    fun submitList(newList: List<DeviceHistoryEntity>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_device_name)
        val tvStatus: TextView = view.findViewById(R.id.tv_device_status)
        val tvDates: TextView = view.findViewById(R.id.tv_dates)
        val tvStats: TextView = view.findViewById(R.id.tv_stats)
        val btnRemove: Button = view.findViewById(R.id.btn_remove_device)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = list[position]
        
        holder.tvName.text = entry.deviceName
        
        val isConnected = OneCallConnectionManager.connectedDevices.value?.any { it.deviceId == entry.deviceId } == true
        if (isConnected) {
            holder.tvStatus.text = "Connected"
            holder.tvStatus.setTextColor(holder.itemView.context.getColor(com.onecall.R.color.onecall_success))
        } else {
            holder.tvStatus.text = "Disconnected"
            holder.tvStatus.setTextColor(holder.itemView.context.getColor(com.onecall.R.color.onecall_gray))
        }

        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val firstConnected = sdf.format(Date(entry.firstConnectedAt))
        val lastSeen = sdf.format(Date(entry.lastSeenAt))
        holder.tvDates.text = "First seen: $firstConnected\nLast seen: $lastSeen"
        
        holder.tvStats.text = "Calls Attended: ${entry.callsAttended} | Calls Made: ${entry.callsMade}"
        
        holder.btnRemove.setOnClickListener { onRemoveClick(entry) }
    }

    override fun getItemCount() = list.size
}

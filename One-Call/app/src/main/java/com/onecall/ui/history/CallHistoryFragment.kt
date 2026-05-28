package com.onecall.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.onecall.R
import com.onecall.data.history.CallHistoryEntity
import com.onecall.data.history.HistoryRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CallHistoryFragment : Fragment(R.layout.fragment_call_history) {

    private lateinit var rvCallHistory: RecyclerView
    private lateinit var tvEmptyHistory: TextView
    private lateinit var btnClearAll: View
    private lateinit var chipGroupFilter: ChipGroup
    private lateinit var repository: HistoryRepository
    
    private val adapter = CallHistoryAdapter(
        onItemClick = { entry ->
            showCallbackDialog(entry)
        },
        onItemLongClick = { entry ->
            showDeleteDialog(entry)
        }
    )

    private var allHistoryList = emptyList<CallHistoryEntity>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        repository = HistoryRepository.getInstance(requireContext())
        
        rvCallHistory = view.findViewById(R.id.rv_call_history)
        tvEmptyHistory = view.findViewById(R.id.tv_empty_history)
        btnClearAll = view.findViewById(R.id.btn_clear_all)
        chipGroupFilter = view.findViewById(R.id.chip_group_filter)

        rvCallHistory.layoutManager = LinearLayoutManager(requireContext())
        rvCallHistory.adapter = adapter

        btnClearAll.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Clear Call History")
                .setMessage("Are you sure you want to delete all call history?")
                .setPositiveButton("Clear") { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        repository.clearCallHistory()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            applyFilter(checkedIds.firstOrNull() ?: R.id.chip_all)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repository.getCallHistory().collectLatest { list ->
                allHistoryList = list
                applyFilter(chipGroupFilter.checkedChipId)
            }
        }
    }

    private fun applyFilter(checkedId: Int) {
        val filteredList = when (checkedId) {
            R.id.chip_incoming -> allHistoryList.filter { it.callType == "INCOMING" }
            R.id.chip_outgoing -> allHistoryList.filter { it.callType == "OUTGOING" }
            R.id.chip_missed -> allHistoryList.filter { it.callType == "MISSED" }
            else -> allHistoryList // R.id.chip_all
        }
        
        adapter.submitList(filteredList)
        
        if (filteredList.isEmpty()) {
            rvCallHistory.visibility = View.GONE
            tvEmptyHistory.visibility = View.VISIBLE
        } else {
            rvCallHistory.visibility = View.VISIBLE
            tvEmptyHistory.visibility = View.GONE
        }
    }

    private fun showCallbackDialog(entry: CallHistoryEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Call back karna chahte ho?")
            .setMessage(entry.phoneNumber)
            .setPositiveButton("Call") { _, _ ->
                (requireActivity() as? com.onecall.MainActivity)?.initiateOutgoingCall(entry.phoneNumber, entry.callerName ?: "")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(entry: CallHistoryEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Entry")
            .setMessage("Delete this call history entry?")
            .setPositiveButton("Delete") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.deleteCallHistory(entry.id)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

class CallHistoryAdapter(
    private val onItemClick: (CallHistoryEntity) -> Unit,
    private val onItemLongClick: (CallHistoryEntity) -> Unit
) : RecyclerView.Adapter<CallHistoryAdapter.ViewHolder>() {

    private var list = listOf<CallHistoryEntity>()

    fun submitList(newList: List<CallHistoryEntity>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.iv_call_type_icon)
        val tvNameNumber: TextView = view.findViewById(R.id.tv_caller_name_or_number)
        val tvDeviceInfo: TextView = view.findViewById(R.id.tv_device_info)
        val tvDateTime: TextView = view.findViewById(R.id.tv_date_time)
        val tvDuration: TextView = view.findViewById(R.id.tv_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_call_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = list[position]
        
        val iconRes = when (entry.callType) {
            "INCOMING" -> android.R.drawable.sym_call_incoming
            "OUTGOING" -> android.R.drawable.sym_call_outgoing
            else -> android.R.drawable.sym_call_missed
        }
        val iconTint = when (entry.callType) {
            "MISSED" -> com.onecall.R.color.onecall_error
            "INCOMING" -> com.onecall.R.color.onecall_blue
            else -> com.onecall.R.color.onecall_success
        }
        holder.ivIcon.setImageResource(iconRes)
        holder.ivIcon.setColorFilter(holder.itemView.context.getColor(iconTint))

        val name = entry.callerName.takeIf { !it.isNullOrBlank() }
        holder.tvNameNumber.text = if (name != null) "$name (${entry.phoneNumber})" else entry.phoneNumber

        val deviceName = entry.attendedByDevice ?: "This Device"
        holder.tvDeviceInfo.text = when (entry.callType) {
            "OUTGOING" -> "Called from: $deviceName"
            "MISSED" -> "Ring count: ${entry.ringCount}"
            else -> "Attended on: $deviceName"
        }

        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        holder.tvDateTime.text = sdf.format(Date(entry.dateTime))

        val duration = entry.durationSeconds
        if (duration > 0) {
            val min = duration / 60
            val sec = duration % 60
            holder.tvDuration.text = if (min > 0) "${min}m ${sec}s" else "${sec}s"
            holder.tvDuration.visibility = View.VISIBLE
        } else {
            holder.tvDuration.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onItemClick(entry) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(entry)
            true
        }
    }

    override fun getItemCount() = list.size
}

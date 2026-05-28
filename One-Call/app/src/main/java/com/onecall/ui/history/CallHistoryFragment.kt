package com.onecall.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onecall.R
import com.onecall.data.db.OneCallDatabase
import com.onecall.data.db.entities.CallHistoryEntity
import com.onecall.data.repository.CallHistoryRepository
import com.onecall.databinding.FragmentCallHistoryBinding
import com.onecall.databinding.ItemCallHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CallHistoryFragment : Fragment() {

    private var _binding: FragmentCallHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: CallHistoryRepository
    private val allHistory = mutableListOf<CallHistoryEntity>()
    private val displayHistory = mutableListOf<CallHistoryEntity>()
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCallHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = CallHistoryRepository(OneCallDatabase.getDatabase(requireContext()).callHistoryDao())

        adapter = HistoryAdapter(displayHistory)
        binding.rvCallHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CallHistoryFragment.adapter
        }

        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            val filter = when {
                R.id.chip_missed in checkedIds -> "MISSED"
                R.id.chip_incoming in checkedIds -> "INCOMING"
                R.id.chip_outgoing in checkedIds -> "OUTGOING"
                else -> null
            }
            applyFilter(filter)
        }

        lifecycleScope.launch {
            repository.allPermanentHistory.collectLatest { history ->
                allHistory.clear()
                allHistory.addAll(history)
                applyFilter(null)
            }
        }
    }

    private fun applyFilter(type: String?) {
        displayHistory.clear()
        if (type == null) {
            displayHistory.addAll(allHistory)
        } else {
            allHistory.filterTo(displayHistory) { it.callType == type }
        }
        adapter.notifyDataSetChanged()
        binding.tvNoHistory.visibility = if (displayHistory.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class HistoryAdapter(
    private val items: List<CallHistoryEntity>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())

    class ViewHolder(val binding: ItemCallHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCallHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvCallerName.text = item.callerName ?: "Unknown"
        holder.binding.tvCallerNumber.text = item.callerNumber
        holder.binding.tvCallTime.text = dateFormat.format(Date(item.timestamp))
        holder.binding.tvCallDuration.text = formatDuration(item.durationSeconds)
        val iconRes = when (item.callType) {
            "MISSED" -> R.drawable.ic_phone_end
            "OUTGOING" -> R.drawable.ic_phone_call
            else -> R.drawable.ic_phone_incoming
        }
        holder.binding.ivCallTypeIcon.setImageResource(iconRes)
    }

    override fun getItemCount() = items.size

    private fun formatDuration(seconds: Long): String {
        if (seconds == 0L) return "Missed"
        val m = seconds / 60
        val s = seconds % 60
        return "${m}m ${s}s"
    }
}

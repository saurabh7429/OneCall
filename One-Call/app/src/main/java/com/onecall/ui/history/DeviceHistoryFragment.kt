package com.onecall.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.onecall.data.db.OneCallDatabase
import com.onecall.data.db.entities.CallHistoryEntity
import com.onecall.data.repository.CallHistoryRepository
import com.onecall.databinding.FragmentDeviceHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DeviceHistoryFragment : Fragment() {

    private var _binding: FragmentDeviceHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDeviceHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = CallHistoryRepository(OneCallDatabase.getDatabase(requireContext()).callHistoryDao())
        val history = mutableListOf<CallHistoryEntity>()
        val adapter = HistoryAdapter(history)

        binding.rvDeviceHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDeviceHistory.adapter = adapter

        lifecycleScope.launch {
            repository.sessionHistory.collectLatest { h ->
                history.clear()
                history.addAll(h)
                adapter.notifyDataSetChanged()
                binding.tvNoHistory.visibility = if (history.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

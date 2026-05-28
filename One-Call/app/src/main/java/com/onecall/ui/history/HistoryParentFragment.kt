package com.onecall.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.onecall.R
import com.onecall.data.DeviceRoleStore

class HistoryParentFragment : Fragment(R.layout.fragment_history_parent) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout_history)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager_history)

        val isMain = DeviceRoleStore.isMain(requireContext())

        val adapter = HistoryPagerAdapter(this, isMain)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Call History"
                1 -> "Device History"
                else -> ""
            }
        }.attach()
        
        if (!isMain) {
            tabLayout.visibility = View.GONE
        }
    }

    private inner class HistoryPagerAdapter(fragment: Fragment, private val isMain: Boolean) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = if (isMain) 2 else 1

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CallHistoryFragment()
                1 -> DeviceHistoryFragment()
                else -> throw IllegalStateException("Invalid position $position")
            }
        }
    }
}

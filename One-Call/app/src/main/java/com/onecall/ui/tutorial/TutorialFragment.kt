package com.onecall.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.onecall.R
import com.onecall.databinding.FragmentTutorialBinding
import com.onecall.databinding.ItemTutorialCardBinding

data class TutorialCard(val titleRes: Int, val descRes: Int)

class TutorialFragment : Fragment() {

    private var _binding: FragmentTutorialBinding? = null
    private val binding get() = _binding!!

    private val cards = listOf(
        TutorialCard(R.string.tutorial_card1_title, R.string.tutorial_card1_desc),
        TutorialCard(R.string.tutorial_card2_title, R.string.tutorial_card2_desc),
        TutorialCard(R.string.tutorial_card3_title, R.string.tutorial_card3_desc),
        TutorialCard(R.string.tutorial_card4_title, R.string.tutorial_card4_desc),
        TutorialCard(R.string.tutorial_card5_title, R.string.tutorial_card5_desc),
    )

    private val dots = mutableListOf<View>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTutorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = TutorialAdapter()
        setupIndicators()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
                if (position == cards.size - 1) {
                    binding.btnNext.text = getString(R.string.tutorial_get_started)
                } else {
                    binding.btnNext.text = getString(R.string.tutorial_next)
                }
            }
        })

        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < cards.size - 1) {
                binding.viewPager.currentItem = current + 1
            } else {
                findNavController().navigate(R.id.action_tutorial_to_welcome)
            }
        }

        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_tutorial_to_welcome)
        }
    }

    private fun setupIndicators() {
        binding.indicatorLayout.removeAllViews()
        dots.clear()
        cards.forEachIndexed { index, _ ->
            val dot = View(requireContext()).apply {
                val size = if (index == 0) 24 else 16
                layoutParams = ViewGroup.MarginLayoutParams(size.dpToPx(), size.dpToPx()).apply {
                    marginStart = 6.dpToPx()
                    marginEnd = 6.dpToPx()
                }
                setBackgroundResource(if (index == 0) R.drawable.ic_connected else R.drawable.ic_disconnected)
            }
            dots.add(dot)
            binding.indicatorLayout.addView(dot)
        }
    }

    private fun updateIndicators(activeIndex: Int) {
        dots.forEachIndexed { index, view ->
            val size = if (index == activeIndex) 24 else 16
            view.layoutParams = ViewGroup.MarginLayoutParams(size.dpToPx(), size.dpToPx()).apply {
                marginStart = 6.dpToPx()
                marginEnd = 6.dpToPx()
            }
            view.setBackgroundResource(
                if (index == activeIndex) R.drawable.ic_connected else R.drawable.ic_disconnected
            )
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    inner class TutorialAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = cards.size
        override fun createFragment(position: Int) = TutorialCardFragment.newInstance(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class TutorialCardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ItemTutorialCardBinding.inflate(inflater, container, false)
        val position = arguments?.getInt("position") ?: 0
        val cards = listOf(
            TutorialCard(R.string.tutorial_card1_title, R.string.tutorial_card1_desc),
            TutorialCard(R.string.tutorial_card2_title, R.string.tutorial_card2_desc),
            TutorialCard(R.string.tutorial_card3_title, R.string.tutorial_card3_desc),
            TutorialCard(R.string.tutorial_card4_title, R.string.tutorial_card4_desc),
            TutorialCard(R.string.tutorial_card5_title, R.string.tutorial_card5_desc),
        )
        val card = cards[position]
        binding.tvTutorialTitle.setText(card.titleRes)
        binding.tvTutorialDesc.setText(card.descRes)
        return binding.root
    }

    companion object {
        fun newInstance(position: Int) = TutorialCardFragment().apply {
            arguments = Bundle().apply { putInt("position", position) }
        }
    }
}

package com.onecall.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.onecall.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DialpadFragment : Fragment(R.layout.fragment_dialpad) {

    private val phoneNumber = MutableStateFlow("")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNumber = view.findViewById<EditText>(R.id.tv_dialpad_number)
        val btnBackspace = view.findViewById<ImageButton>(R.id.btn_backspace)
        val rvDialpad = view.findViewById<RecyclerView>(R.id.rv_dialpad)
        val btnCall = view.findViewById<FloatingActionButton>(R.id.btn_call)

        setupDialpadGrid(rvDialpad)

        tvNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                phoneNumber.value = s.toString()
            }
        })

        btnBackspace.setOnClickListener {
            val current = tvNumber.text.toString()
            if (current.isNotEmpty()) {
                val selectionEnd = tvNumber.selectionEnd
                if (selectionEnd > 0) {
                    val sb = StringBuilder(current).deleteCharAt(selectionEnd - 1)
                    tvNumber.setText(sb.toString())
                    tvNumber.setSelection(selectionEnd - 1)
                }
            }
        }

        btnBackspace.setOnLongClickListener {
            tvNumber.setText("")
            true
        }

        phoneNumber.onEach {
            btnCall.isEnabled = it.isNotBlank()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        btnCall.setOnClickListener {
            val number = phoneNumber.value
            if (number.isNotBlank()) {
                (requireActivity() as? com.onecall.MainActivity)?.initiateOutgoingCall(number, "Contact")
            }
        }
    }

    private fun setupDialpadGrid(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        val keys = listOf(
            DialpadKey("1", ""),
            DialpadKey("2", "ABC"),
            DialpadKey("3", "DEF"),
            DialpadKey("4", "GHI"),
            DialpadKey("5", "JKL"),
            DialpadKey("6", "MNO"),
            DialpadKey("7", "PQRS"),
            DialpadKey("8", "TUV"),
            DialpadKey("9", "WXYZ"),
            DialpadKey("*", ""),
            DialpadKey("0", "+"),
            DialpadKey("#", "")
        )
        rv.adapter = DialpadAdapter(keys) { key, isLongPress ->
            val tvNumber = view?.findViewById<EditText>(R.id.tv_dialpad_number) ?: return@DialpadAdapter
            val current = tvNumber.text.toString()
            val selectionEnd = tvNumber.selectionEnd
            
            val charToAdd = if (isLongPress && key.digit == "0") "+" else key.digit
            
            val sb = StringBuilder(current).insert(if (selectionEnd >= 0) selectionEnd else current.length, charToAdd)
            tvNumber.setText(sb.toString())
            tvNumber.setSelection((if (selectionEnd >= 0) selectionEnd else current.length) + 1)
        }
    }
}

data class DialpadKey(val digit: String, val letters: String)

class DialpadAdapter(
    private val keys: List<DialpadKey>,
    private val onKeyClick: (DialpadKey, Boolean) -> Unit
) : RecyclerView.Adapter<DialpadAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDigit: TextView = view.findViewById(R.id.tv_digit)
        val tvLetters: TextView = view.findViewById(R.id.tv_letters)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dialpad_key, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = keys[position]
        holder.tvDigit.text = key.digit
        
        if (key.letters.isNotEmpty()) {
            holder.tvLetters.text = key.letters
            holder.tvLetters.visibility = View.VISIBLE
        } else {
            holder.tvLetters.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onKeyClick(key, false)
        }
        
        holder.itemView.setOnLongClickListener {
            onKeyClick(key, true)
            true
        }
    }

    override fun getItemCount() = keys.size
}

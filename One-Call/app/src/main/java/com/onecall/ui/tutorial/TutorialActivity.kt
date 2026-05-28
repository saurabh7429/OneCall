package com.onecall.ui.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.onecall.MainActivity
import com.onecall.R

class TutorialActivity : AppCompatActivity() {

    private data class TutorialSlide(val title: String, val desc: String, val iconRes: Int)

    private val slides = listOf(
        TutorialSlide("Welcome to OneCall", "Make and receive calls on all your devices over local Wi-Fi. No internet required.", android.R.drawable.ic_dialog_info),
        TutorialSlide("Main Device", "The phone with your SIM card acts as the Main Device. Generate a code to connect others.", android.R.drawable.ic_menu_agenda),
        TutorialSlide("Connect Devices", "Enter the 6-digit code on your other phones or tablets on the same Wi-Fi network.", android.R.drawable.ic_menu_share),
        TutorialSlide("Calls", "When a call comes in, all devices ring. Answer or make calls from any connected device.", android.R.drawable.ic_menu_call),
        TutorialSlide("Ready!", "Let's set up your device and grant the necessary permissions.", android.R.drawable.ic_dialog_dialer)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = getSharedPreferences("onecall_prefs", Context.MODE_PRIVATE)
        val isManualLaunch = intent.getBooleanExtra("manual_launch", false)
        if (prefs.getBoolean("tutorial_shown", false) && !isManualLaunch) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_tutorial)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager_tutorial)
        val btnSkip = findViewById<MaterialButton>(R.id.btn_skip)
        val btnBack = findViewById<MaterialButton>(R.id.btn_back)
        val btnNext = findViewById<MaterialButton>(R.id.btn_next)

        viewPager.adapter = TutorialAdapter(slides)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                btnBack.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                btnNext.text = if (position == slides.size - 1) "Get Started" else "Next"
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < slides.size - 1) {
                viewPager.currentItem += 1
            } else {
                finishTutorial()
            }
        }

        btnBack.setOnClickListener {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1
            }
        }

        btnSkip.setOnClickListener {
            finishTutorial()
        }
    }

    private fun finishTutorial() {
        val prefs = getSharedPreferences("onecall_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("tutorial_shown", true).apply()
        
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private inner class TutorialAdapter(private val items: List<TutorialSlide>) : RecyclerView.Adapter<TutorialAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tutorial_card, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.title.text = item.title
            holder.desc.text = item.desc
            holder.icon.setImageResource(item.iconRes)
        }

        override fun getItemCount() = items.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.tv_tutorial_title)
            val desc: TextView = view.findViewById(R.id.tv_tutorial_desc)
            val icon: ImageView = view.findViewById(R.id.iv_tutorial_image)
        }
    }
}

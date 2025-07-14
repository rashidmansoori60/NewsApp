package com.example.kotlinapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlinapp.adapter.ViewPagerAdapter
import com.example.kotlinapp.databinding.ActivityMainBinding
import com.example.kotlinapp.ui.viewmodel.NewsViewModel

import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.example.kotlinapp.R.id.mainAct)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.BLACK // Pure black color
        }

    var adapterr =ViewPagerAdapter(this)
    binding.viewPager.adapter=adapterr

        setSupportActionBar(binding.toolbar)
        val tabLayout =binding.tabLayout



        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Headline"
                1 -> "Business"
                2 -> "Sport"
                3 -> "Entertainment"
                4 -> "General"
                5 -> "Science"
                6 -> "Health"
                else -> "Tab ${position + 1}"
            }
        }.attach()


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                supportActionBar?.title = when (position) {
                    0 -> "Headline"
                    1 -> "Business"
                    2 -> "Sport"
                    3 -> "Entertainment"
                    4 -> "General"
                    5 -> "Science"
                    6 -> "Health"
                    else -> "News App"
                }
            }


        })


    }

}

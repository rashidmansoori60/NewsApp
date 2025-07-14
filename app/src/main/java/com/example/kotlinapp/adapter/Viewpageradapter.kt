package com.example.kotlinapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kotlinapp.ui.fragment.BusinessFragment
import com.example.kotlinapp.ui.fragment.EntertainmentFragment
import com.example.kotlinapp.ui.fragment.GeneralFragment
import com.example.kotlinapp.ui.fragment.HeadlineFragment
import com.example.kotlinapp.ui.fragment.HealthFragment
import com.example.kotlinapp.ui.fragment.ScienceFragment
import com.example.kotlinapp.ui.fragment.sportFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        HeadlineFragment(),
        BusinessFragment(),
        sportFragment(),
        EntertainmentFragment(),
        GeneralFragment(),
        ScienceFragment(),
        HealthFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}

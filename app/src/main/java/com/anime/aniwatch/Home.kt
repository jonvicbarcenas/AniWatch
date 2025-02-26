package com.anime.aniwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.Calendar

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Set up the ViewPager and TabLayout
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

        val fragmentAdapter = FragmentAdapter(childFragmentManager) // Use childFragmentManager for fragments inside fragments
        fragmentAdapter.addFragment(Day(), "Today")
        fragmentAdapter.addFragment(Week(), "Week")
        fragmentAdapter.addFragment(Month(), "Month")

        // Set the adapter to ViewPager
        viewPager.adapter = fragmentAdapter

        // Link ViewPager to TabLayout
        tabLayout.setupWithViewPager(viewPager)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeMsg: TextView = view.findViewById(R.id.homeMsg)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 0..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }
        homeMsg.text = greeting
    }


}

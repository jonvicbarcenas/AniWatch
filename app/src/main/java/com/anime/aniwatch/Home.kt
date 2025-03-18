package com.anime.aniwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {

    private lateinit var imageSliderViewPager: ViewPager
    private lateinit var fragmentViewPager: ViewPager
    private lateinit var handler: Handler
    private lateinit var imageAdapter: SliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Get images from MovieData
        val imageList = MovieData.seriesImages

        // Set up the ViewPager for the image slider
        imageSliderViewPager = view.findViewById(R.id.imageSliderViewPager)
        imageAdapter = SliderAdapter(requireContext(), imageList)
        imageSliderViewPager.adapter = imageAdapter

        // Set up the auto-slide functionality for the image slider
        handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = imageSliderViewPager.currentItem
                val nextItem = if (currentItem + 1 < imageList.size) currentItem + 1 else 0
                imageSliderViewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000) // Slide every 3 seconds
            }
        }
        handler.post(runnable)

        // Set up the TabLayout and ViewPager for fragments (Day, Week, Month)
        fragmentViewPager = view.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val fragmentAdapter = FragmentAdapter(childFragmentManager) // Use childFragmentManager for fragments inside fragments
        fragmentAdapter.addFragment(Day(), "Today")
        fragmentAdapter.addFragment(Week(), "Week")
        fragmentAdapter.addFragment(Month(), "Month")

        // Set the adapter to ViewPager for the fragments
        fragmentViewPager.adapter = fragmentAdapter

        // Link ViewPager to TabLayout
        tabLayout.setupWithViewPager(fragmentViewPager)

        return view
    }

    // Remove handler to prevent memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}

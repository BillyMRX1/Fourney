package com.bearbrand.fourney.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bearbrand.fourney.ui.splash.screen.SecondScreenFragment
import com.bearbrand.fourney.ui.splash.screen.StartAuthFragment
import com.bearbrand.fourney.ui.splash.screen.ThirdScreenFragment


class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val fragmentList = listOf(
        SecondScreenFragment(),
        ThirdScreenFragment(),
        StartAuthFragment()
    )
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

}
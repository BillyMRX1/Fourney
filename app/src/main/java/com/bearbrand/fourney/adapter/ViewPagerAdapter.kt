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
    private val fragmentList: MutableList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = SecondScreenFragment()
            1 -> fragment = ThirdScreenFragment()
            2 -> fragment = StartAuthFragment()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = fragmentList.size

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

//    private val fragmentList = listOf(
//        SecondScreenFragment(),
//        ThirdScreenFragment(),
//        StartAuthFragment()
//    )
//    override fun getCount(): Int {
//        return fragmentList.size
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return fragmentList[position]
//    }

}
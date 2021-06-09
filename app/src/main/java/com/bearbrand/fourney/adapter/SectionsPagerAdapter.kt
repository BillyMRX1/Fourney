package com.bearbrand.fourney.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bearbrand.fourney.ui.recommendation.AllPlaceFragment
import com.bearbrand.fourney.ui.recommendation.RecommendationFragment

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val titleList: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = RecommendationFragment()
            1 -> fragment = AllPlaceFragment()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}
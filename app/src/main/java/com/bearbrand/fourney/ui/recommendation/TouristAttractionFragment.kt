package com.bearbrand.fourney.ui.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bearbrand.fourney.adapter.SectionPagerAdapter
import com.bearbrand.fourney.databinding.FragmentTouristAttractionBinding
import com.google.android.material.tabs.TabLayout


class TouristAttractionFragment : Fragment() {
    private lateinit var binding: FragmentTouristAttractionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTouristAttractionBinding.inflate(inflater, container, false)

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager(binding.viewPager)
        binding.tabs.setupWithViewPager(binding.viewPager)
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setUpViewPager(viewPager: ViewPager?) {
        val adapter = SectionPagerAdapter(childFragmentManager)
        adapter.addFragment(RecommendationFragment(), "Recommendation")
        adapter.addFragment(AllPlaceFragment(), "Semua Tempat")
        viewPager!!.adapter = adapter
    }

}
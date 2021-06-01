package com.bearbrand.fourney.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bearbrand.fourney.R
import com.bearbrand.fourney.adapter.ViewPagerAdapter
import com.bearbrand.fourney.databinding.FragmentViewPagerBinding
import com.bearbrand.fourney.ui.splash.screen.FirstScreenFragment
import com.bearbrand.fourney.ui.splash.screen.SecondScreenFragment
import com.bearbrand.fourney.ui.splash.screen.StartAuthFragment
import com.bearbrand.fourney.ui.splash.screen.ThirdScreenFragment

class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)

        binding.viewPager.adapter = adapter

        return binding.root
    }
}
package com.bearbrand.fourney.ui.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bearbrand.fourney.adapter.ViewPagerAdapter
import com.bearbrand.fourney.databinding.FragmentViewPagerBinding
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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager(binding.viewPager)

    }

    private fun setUpViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(SecondScreenFragment())
        adapter.addFragment(ThirdScreenFragment())
        adapter.addFragment(StartAuthFragment())
        viewPager.adapter = adapter
    }


//    private fun onStartAuth(): Boolean{
//        val sharedPref = requireActivity().getSharedPreferences("startAuth", Context.MODE_PRIVATE)
//        return sharedPref.getBoolean("Start", false)
//    }
}
package com.bearbrand.fourney.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bearbrand.fourney.adapter.ViewPagerAdapter
import com.bearbrand.fourney.databinding.FragmentViewPagerBinding

class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        adapter.notifyDataSetChanged()

        binding.viewPager.adapter = adapter

        return binding.root
    }



//    private fun onStartAuth(): Boolean{
//        val sharedPref = requireActivity().getSharedPreferences("startAuth", Context.MODE_PRIVATE)
//        return sharedPref.getBoolean("Start", false)
//    }
}
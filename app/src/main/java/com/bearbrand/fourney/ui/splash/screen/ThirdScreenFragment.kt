package com.bearbrand.fourney.ui.splash.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentThirdScreenBinding

class ThirdScreenFragment : Fragment() {
    private lateinit var binding: FragmentThirdScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdScreenBinding.inflate(inflater, container, false)

        //requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)

        val viewPager = activity?.findViewById<ViewPager>(R.id.viewPager)
        binding.btnBerikutnya.setOnClickListener {
            viewPager?.currentItem = 2
            onBoardingFinish()
        }
        return binding.root
    }

    private fun onBoardingFinish() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}
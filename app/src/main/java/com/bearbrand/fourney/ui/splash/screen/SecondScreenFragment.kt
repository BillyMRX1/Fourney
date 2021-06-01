package com.bearbrand.fourney.ui.splash.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentSecondScreenBinding

class SecondScreenFragment : Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        //requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)

        val viewPager = activity?.findViewById<ViewPager>(R.id.viewPager)
        binding.btnBerikutnya.setOnClickListener {
            viewPager?.currentItem = 1
        }
        return binding.root
    }
}
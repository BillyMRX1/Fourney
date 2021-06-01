package com.bearbrand.fourney.ui.splash.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentFirstScreenBinding

class FirstScreenFragment : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false)

        //requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = resources.getColor(R.color.blue)

        binding.btnMulaiSekarang.setOnClickListener {
            findNavController().navigate(R.id.action_firstScreenFragment_to_viewPagerFragment)
        }
        return binding.root
    }
}
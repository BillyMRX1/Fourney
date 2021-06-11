package com.bearbrand.fourney.ui.splash.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.bearbrand.fourney.MenuActivity
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentStartAuthBinding
import com.bearbrand.fourney.ui.auth.LoginFragment
import com.bearbrand.fourney.ui.auth.RegisterFragment
import com.bearbrand.fourney.ui.splash.ViewPagerFragmentDirections


class StartAuthFragment : Fragment() {
    private lateinit var binding: FragmentStartAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartAuthBinding.inflate(inflater, container, false)
        onBoardingFinish()
        val mFragmentManager = fragmentManager

        requireActivity().window.statusBarColor = resources.getColor(R.color.white)

        binding.btnDaftar.setOnClickListener {
            if(openedMain() || fromSplash()){
                val mFragmentDaftar = RegisterFragment()

                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragmentContainerView, mFragmentDaftar, RegisterFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }else{
                findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToRegisterFragment())
            }

        }

        binding.btnMasuk.setOnClickListener {
            if(openedMain() || fromSplash()){
                val mFragmentMasuk = LoginFragment()

                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragmentContainerView, mFragmentMasuk, LoginFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }else{
                findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToLoginFragment())
            }
        }
        binding.tvLewati.setOnClickListener {
            val intent =
                Intent(context, MenuActivity::class.java)
            startActivity(intent)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            requireActivity().finish()
            openMain()
        }

        return binding.root
    }

    private fun fromSplash(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("fromSplash", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    private fun onBoardingFinish() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    private fun openMain() {
        val sharedPref = requireActivity().getSharedPreferences("openMain", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    private fun openedMain(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("openMain", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

//    private fun startAuthOpened() {
//        val sharedPref = requireActivity().getSharedPreferences("startAuth", Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//        editor.putBoolean("Start", true)
//        editor.apply()
//    }


}
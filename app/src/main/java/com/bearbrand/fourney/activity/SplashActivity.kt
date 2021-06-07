package com.bearbrand.fourney.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentTransaction
import com.bearbrand.fourney.MenuActivity
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivitySplashBinding
import com.bearbrand.fourney.ui.auth.LoginFragment
import com.bearbrand.fourney.ui.splash.SplashFragment
import com.bearbrand.fourney.ui.splash.screen.StartAuthFragment
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    var value: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
//        val mFragmentManager = supportFragmentManager

//        val extras = getIntent().getExtras()
//        if (null != extras) {
//            value = extras.getString("start").toString()
//        }
//
//        if(value.equals("startAuth")){
//            val mFragmentAuth = StartAuthFragment()
//
//            mFragmentManager?.beginTransaction()?.apply {
//                replace(R.id.fragmentContainerView, mFragmentAuth, StartAuthFragment::class.java.simpleName)
//                addToBackStack(null)
//                commit()
//            }
//        }else{
//            val mFragmentSplash = SplashFragment()
//
//            mFragmentManager?.beginTransaction()?.apply {
//                replace(R.id.fragmentContainerView, mFragmentSplash, SplashFragment::class.java.simpleName)
//                addToBackStack(null)
//                commit()
//            }
//
//        }


        setContentView(binding.root)
    }



}
package com.bearbrand.fourney.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityAuthBinding
import com.bearbrand.fourney.ui.splash.screen.StartAuthFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        val mFragmentManager = supportFragmentManager
        val mFragmentAuth = StartAuthFragment()



        mFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, mFragmentAuth, StartAuthFragment::class.java.simpleName)
            commit()
        }

        setContentView(binding.root)
    }
}
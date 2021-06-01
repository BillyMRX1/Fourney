package com.bearbrand.fourney.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bearbrand.fourney.MenuActivity
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        checkUser()
    }

//    private fun checkUser() {
//        val user = FirebaseAuth.getInstance().currentUser
//        Handler(Looper.getMainLooper()).postDelayed({
//            if (user != null) {
//                startActivity(Intent(applicationContext, MenuActivity::class.java))
//                finish()
//            } else {
//                startActivity(Intent(applicationContext, MenuActivity::class.java))
//                finish()
//            }
//        }, 2000)
//    }
}
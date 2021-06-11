package com.bearbrand.fourney.ui.splash

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.navigation.fragment.findNavController
import com.bearbrand.fourney.MenuActivity
import com.bearbrand.fourney.R
import com.bearbrand.fourney.activity.AuthActivity
import com.bearbrand.fourney.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        //requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = resources.getColor(R.color.blue)

        Handler(Looper.getMainLooper()).postDelayed({
            if(onBoardingFinished()){
                checkUser()
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_firstScreenFragment)
            }
        }, 2000)

        return binding.root
    }

    private fun checkUser() {
        val user = FirebaseAuth.getInstance().currentUser
        Handler(Looper.getMainLooper()).postDelayed({
            if (user != null) {
                startActivity(Intent(requireContext(), MenuActivity::class.java))
                requireActivity().finish()
            } else {
//                if (isOpenMain()){
                    val intent =
                        Intent(context, AuthActivity::class.java)
                    startActivity(intent)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    requireActivity().finish()
                fromSplash()
//                }else{
//                    findNavController().navigate(R.id.action_splashFragment_to_startAuthFragment)
//                }
            }
        }, 2000)
    }

    private fun fromSplash() {
            val sharedPref = requireActivity().getSharedPreferences("fromSplash", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("Finished", true)
            editor.apply()

    }

    private fun onBoardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    private fun isOpenMain(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("openMain", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

}
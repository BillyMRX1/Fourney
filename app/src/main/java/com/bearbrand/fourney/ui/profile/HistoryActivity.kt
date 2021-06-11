package com.bearbrand.fourney.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityHistoryBinding
import com.bearbrand.fourney.ui.history.HistoryFragment

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mFragmentManager = supportFragmentManager
        val mHistoryFragment = HistoryFragment()
        val fragment = mFragmentManager.findFragmentByTag(HistoryFragment::class.java.simpleName)
        if (fragment !is HistoryFragment) {
            mFragmentManager
                .beginTransaction()
                .add(R.id.fragment, mHistoryFragment, HistoryFragment::class.java.simpleName)
                .commit()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }


    }
}
package com.bearbrand.fourney.ui.reward.tiketku.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityUsedTicketBinding

class UsedTicketActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUsedTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsedTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
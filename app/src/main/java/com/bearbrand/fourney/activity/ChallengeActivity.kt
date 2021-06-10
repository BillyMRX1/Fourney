package com.bearbrand.fourney.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityChallengeBinding
import com.bearbrand.fourney.ui.challenge.ChallengeFragment

class ChallengeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
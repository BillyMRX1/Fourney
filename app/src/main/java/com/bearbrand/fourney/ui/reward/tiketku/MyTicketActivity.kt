package com.bearbrand.fourney.ui.reward.tiketku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityMyTicketBinding
import com.bearbrand.fourney.ui.reward.TiketAdapter
import com.bearbrand.fourney.utils.DummyTiket

class MyTicketActivity : AppCompatActivity() {

    private lateinit var adapter: TiketkuAdapter
    private lateinit var binding: ActivityMyTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val listItem = DummyTiket.getMyTicket()
            adapter = TiketkuAdapter(listItem)
            rvMyTicket.adapter = adapter
            adapter.notifyDataSetChanged()
            rvMyTicket.layoutManager = LinearLayoutManager(
                this@MyTicketActivity,
                LinearLayoutManager.VERTICAL, false
            )
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}
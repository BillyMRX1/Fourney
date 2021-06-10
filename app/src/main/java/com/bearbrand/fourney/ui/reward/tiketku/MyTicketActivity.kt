package com.bearbrand.fourney.ui.reward.tiketku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityMyTicketBinding
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.ui.reward.TiketAdapter
import com.bearbrand.fourney.utils.DummyTiket

class MyTicketActivity : AppCompatActivity() {

    companion object {
        const val USER_KUPON = "user_kupon"
    }

    private lateinit var adapter: TiketkuAdapter
    private lateinit var binding: ActivityMyTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            layoutEmpty.visibility = View.VISIBLE
            intent.getParcelableArrayListExtra<TiketModel>(USER_KUPON)?.let {
                layoutEmpty.visibility = View.GONE
                adapter = TiketkuAdapter(it)
                rvMyTicket.adapter = adapter
                adapter.notifyDataSetChanged()
                rvMyTicket.layoutManager = LinearLayoutManager(
                    this@MyTicketActivity,
                    LinearLayoutManager.VERTICAL, false
                )
            }

            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}
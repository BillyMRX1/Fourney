package com.bearbrand.fourney.ui.history.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityDetailHistoryBinding
import com.bearbrand.fourney.model.DummyHistoryModel
import com.bearbrand.fourney.model.HistoryModel
import com.bearbrand.fourney.ui.history.HistoryAdapter
import com.bearbrand.fourney.utils.DummyHistory

class DetailHistoryActivity : AppCompatActivity() {
    companion object{
        val DETAIL = "detail"
    }
    private lateinit var binding : ActivityDetailHistoryBinding
    private lateinit var adapter : DetailHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentValue = intent.getParcelableExtra<DummyHistoryModel>(DETAIL)

        with(binding){
            btnBack.setOnClickListener { finish() }

            tvLocation.text = intentValue?.idPlace
            val dummyList = DummyHistory.generateDummyDetail()
            adapter = DetailHistoryAdapter(dummyList)
            rvDetailHistory.adapter = adapter
            adapter.notifyDataSetChanged()
            rvDetailHistory.layoutManager = LinearLayoutManager(
                this@DetailHistoryActivity,
                LinearLayoutManager.VERTICAL, false
            )
        }
    }
}
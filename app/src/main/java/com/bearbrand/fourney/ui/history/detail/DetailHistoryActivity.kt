package com.bearbrand.fourney.ui.history.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.databinding.ActivityDetailHistoryBinding
import com.bearbrand.fourney.model.DetailObjectEntity
import com.bearbrand.fourney.model.HistoryEntity
import com.bearbrand.fourney.utils.DummyHistory

class DetailHistoryActivity : AppCompatActivity() {
    companion object {
        val DETAIL = "detail"
    }

    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var adapter: DetailHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentValue = intent.getParcelableExtra<HistoryEntity>(DETAIL)

        with(binding) {

            intentValue?.let {

                val textNotFinished =
                    "Misi anda belum selesai, silahkan lanjutkan melalui halaman challenge."
                val textFinished = "Selamat, misi anda telah selesai. lanjutkan petualangan lainnya"
                if (intentValue.listObject!!.size == intentValue.listIdObjectDone!!.size) {
                    textViewKeterangan.text = textFinished
                } else {
                    textViewKeterangan.text = textNotFinished
                }
                tvLocation.text = intentValue.placeName
                val listDetailObject = ArrayList<DetailObjectEntity>()
                for (item in intentValue.listObject) {
                    listDetailObject.add(
                        DetailObjectEntity(
                            item.title,
                            item.coint,
                            checkIdDone(item.id, intentValue.listIdObjectDone)
                        )
                    )
                }
                listDetailObject.sortByDescending {it.done}
                adapter = DetailHistoryAdapter(listDetailObject)
                rvDetailHistory.adapter = adapter
                adapter.notifyDataSetChanged()
                rvDetailHistory.layoutManager = LinearLayoutManager(
                    this@DetailHistoryActivity,
                    LinearLayoutManager.VERTICAL, false
                )
            }



            btnBack.setOnClickListener {
                finish()
            }


        }
    }
}

fun checkIdDone(itemId: String, ItemDoneId: ArrayList<String>): Boolean {
    var defaultValue = false
    ItemDoneId.forEach {
        if (itemId.equals(it)) {
            defaultValue = true
        }
    }
    return defaultValue
}

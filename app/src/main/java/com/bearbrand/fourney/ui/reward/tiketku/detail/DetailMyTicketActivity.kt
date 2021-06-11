package com.bearbrand.fourney.ui.reward.tiketku.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityDetailTicketBinding
import com.bearbrand.fourney.databinding.ActivityDetailTiketkuBinding
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.ui.reward.detail.DetailTicketActivity

class DetailMyTicketActivity : AppCompatActivity() {
    companion object {
        const val TIKETKU = "tiketku"
    }

    private lateinit var binding: ActivityDetailTiketkuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTiketkuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentValue = intent.getParcelableExtra<TiketModel>(TIKETKU)
        with(binding) {
            intentValue?.let {

                tvVoucherPlace.text = it.voucherPlace
                tvValidUntil.text = it.validUntil
                tvVoucherTitle.text = it.voucherTitle
                when (it.background) {
                    "bg_tiket" -> constraintLayout.setBackgroundResource(R.drawable.bg_tiket)
                    "bg_tiket2" -> constraintLayout.setBackgroundResource(R.drawable.bg_tiket2)
                    "bg_tiket3" -> constraintLayout.setBackgroundResource(R.drawable.bg_tiket3)
                }

                val listSnK = listOf(
                    "Dapatkan potongan ${it.voucherTitle} dari harga tiket masuk tempat pariwisata",
                    "Voucher ${it.voucherTitle} bernilai hingga maksimal Rp20.000 (Dua puluh ribu rupiah)",
                    "Berlaku untuk semua tempat wisata yang telah bekerja sama dengan Fourney",
                    "Voucher hanya akan berlaku untuk 1 (satu) pengguna ",
                    "Pengguna diharapkan memperhatikan tanggal kadualrsa dari voucher yang akan ditukarkan",
                    "Voucher dapat ditukarkan dengan menunjukan kode voucher pada saat membeli tiket masuk di tempat wisata terkait "
                )
                var count = 1
                var snk = ""
                for (i in listSnK) {
                    snk += "$count. $i \n"
                    count++
                }
                tvSdk.text = snk

            }
            btnUseVoucher.setOnClickListener {
                startActivity(Intent(this@DetailMyTicketActivity, UsedTicketActivity::class.java))
            }
            btnBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}
package com.bearbrand.fourney.ui.reward.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityDetailTicketBinding
import com.bearbrand.fourney.model.TiketModel

class DetailTicketActivity : AppCompatActivity() {
    companion object {
        const val TIKET = "tiket"
    }

    private lateinit var binding: ActivityDetailTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentValue = intent.getParcelableExtra<TiketModel>(TIKET)
        with(binding) {
            intentValue?.let {

                tvVoucherPlace.text = it.voucherPlace
                tvValidUntil.text = it.validUntil
                tvCoin.text = it.coin.toString()
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
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}
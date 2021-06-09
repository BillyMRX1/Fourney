package com.bearbrand.fourney.ui.reward.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityDetailTicketBinding

class DetailTicketActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val discount = 25
        val listSnK = listOf(
            "Dapatkan diskon sebesar $discount% dari harga tiket masuk tempat pariwisata",
            "Voucher diskon hingga $discount% bernilai hingga maksimal Rp20.000 (Dua puluh ribu rupiah)",
            "Berlaku untuk semua tempat wisata yang telah bekerja sama dengan Fourney",
            "Voucher hanya akan berlaku untuk 1 (satu) pengguna ",
            "Pengguna diharapkan memperhatikan tanggal kadualrsa dari voucher yang akan ditukarkan",
            "Voucher dapat ditukarkan dengan menunjukan kode voucher pada saat membeli tiket masuk di tempat wisata terkait "
        )
        var count = 1
        var snk = ""
        for (i in listSnK){
            snk+="$count. $i \n"
            count++
        }
        binding.tvSdk.text = snk

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
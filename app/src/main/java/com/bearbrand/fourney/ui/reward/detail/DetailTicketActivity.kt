package com.bearbrand.fourney.ui.reward.detail

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bearbrand.fourney.R
import com.bearbrand.fourney.activity.AuthActivity
import com.bearbrand.fourney.databinding.ActivityDetailTicketBinding
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.ui.reward.RewardViewModel
import com.google.firebase.auth.FirebaseAuth

class DetailTicketActivity : AppCompatActivity() {
    companion object {
        const val TIKET = "tiket"
    }

    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val viewModel: RewardViewModel by viewModels()
    private lateinit var binding: ActivityDetailTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentValue = intent.getParcelableExtra<TiketModel>(TIKET)
        with(binding) {
            intentValue?.let { kupon ->

                tvVoucherPlace.text = kupon.voucherPlace
                tvValidUntil.text = kupon.validUntil
                tvCoin.text = kupon.coin.toString()
                tvVoucherTitle.text = kupon.voucherTitle
                when (kupon.background) {
                    "bg_tiket" -> constraintLayout.setBackgroundResource(R.drawable.bg_tiket)
                    "bg_tiket2" -> constraintLayout.setBackgroundResource(R.drawable.bg_tiket2)
                    "bg_tiket3" -> constraintLayout.setBackgroundResource(R.drawable.bg_tiket3)
                }

                val listSnK = listOf(
                    "Dapatkan potongan ${kupon.voucherTitle} dari harga tiket masuk tempat pariwisata",
                    "Voucher ${kupon.voucherTitle} bernilai hingga maksimal Rp20.000 (Dua puluh ribu rupiah)",
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

                uid?.let { uid ->
                    viewModel.getUser(uid).observe(this@DetailTicketActivity, {
                        btnAddKupon.setOnClickListener { _ ->
                            val dialog = AlertDialog.Builder(this@DetailTicketActivity)
                            dialog.setTitle("Pembelian Kupon")
                            dialog.setMessage("Apakah kamu yakin ingin membeli kupon ini?")
                            dialog.setPositiveButton("Iya") { dialog: DialogInterface?, which: Int ->
                                if (it.point > kupon.coin) {
                                    viewModel.addToUserKupon(kupon.kuponId, uid,kupon.coin)
                                    Toast.makeText(this@DetailTicketActivity,"Pembelian tiketmu berhasil",Toast.LENGTH_SHORT).show()
                                    Toast.makeText(this@DetailTicketActivity,"Silahkan reload ulang halaman reward untuk melihat perubahan",Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@DetailTicketActivity,
                                        "Maaf Coin Pointmu belum mencukupi",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            dialog.setNegativeButton("Tidak") { dialog: DialogInterface?, which: Int -> }
                            dialog.show()
                        }

                    })

                }
            }
            btnBack.setOnClickListener {
                finish()
            }

        }


    }
}
package com.bearbrand.fourney.utils

import com.bearbrand.fourney.model.TiketModel

object DummyTiket {
    fun getTiket():ArrayList<TiketModel>{
        val listTiket = ArrayList<TiketModel>()

        listTiket.add(
            TiketModel(
                "bg_tiket",
                80,
                "Discount 20%",
                "Tiket Masuk Pariwisata",
                "December 25, 2021",
            )
        )
        return listTiket
    }
    fun getMyTicket():ArrayList<TiketModel>{
        val listTiket = ArrayList<TiketModel>()
        listTiket.add(
            TiketModel(
                    "bg_tiket",
                80,
                "December 25, 2021",
                "Tiket Masuk Pariwisata",
                "Discount 20%",


            )
        )
        return listTiket
    }
}
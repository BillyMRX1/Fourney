package com.bearbrand.fourney.utils

import com.bearbrand.fourney.model.TiketModel

object DummyTiket {
    fun getTiket():ArrayList<TiketModel>{
        val listTiket = ArrayList<TiketModel>()

        listTiket.add(
            TiketModel(
                "Discount 20%",
                "Tiket Masuk Pariwisata",
                "December 25, 2021",
                80,
                "adpjasdklasfasfasdasd",
                "bg_tiket"
            )
        )
        listTiket.add(
            TiketModel(
                "Discount 30%",
                "Tiket Masuk Pariwisata",
                "December 25, 2021",
                100,
                "adpjasdklasfasfasdasd",
                "bg_tiket2"
            )
        )
        listTiket.add(
            TiketModel(
                "Discount 35%",
                "Tiket Masuk Pariwisata",
                "December 25, 2021",
                125,
                "adpjasdklasfasfasdasd",
                "bg_tiket3"
            )
        )
        return listTiket
    }
    fun getMyTicket():ArrayList<TiketModel>{
        val listTiket = ArrayList<TiketModel>()


        listTiket.add(
            TiketModel(
                "Discount 30%",
                "Tiket Masuk Pariwisata",
                "December 25, 2021",
                100,
                "adpjasdklasfasfasdasd",
                "bg_tiket2"
            )
        )
        listTiket.add(
            TiketModel(
                "Discount 35%",
                "Tiket Masuk Pariwisata",
                "December 25, 2021",
                125,
                "adpjasdklasfasfasdasd",
                "bg_tiket3"
            )
        )
        return listTiket
    }
}
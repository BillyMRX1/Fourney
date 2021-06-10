package com.bearbrand.fourney.utils

import com.bearbrand.fourney.model.DummyObjectModel
import com.bearbrand.fourney.model.HistoryModel

object DummyHistory {
    fun generateDummyHistory():ArrayList<HistoryModel>{
        val listDummy = ArrayList<HistoryModel>()

        listDummy.add(
            HistoryModel(
                "Jatim Park",
                "30 Desember 2020",
                20,
                200,
                2,
                3
            )
        )

        listDummy.add(
            HistoryModel(
                "Dufan",
                "31 Desember 2020",
                20,
                200,
                3,
                3
            )
        )

        return listDummy
    }

    fun generateDummyDetail() : ArrayList<DummyObjectModel>{
        val dummyList = ArrayList<DummyObjectModel>()

        dummyList.add(
            DummyObjectModel(
            "Pintu Masuk",
            5,
            true
        )
        )

        dummyList.add(DummyObjectModel(
            "Pintu Masuk",
            5,
            false
        ))

        dummyList.add(DummyObjectModel(
            "Pintu Masuk",
            5,
            false
        ))

        return dummyList
    }

}
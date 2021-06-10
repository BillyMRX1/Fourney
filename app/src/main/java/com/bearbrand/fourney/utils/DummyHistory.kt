package com.bearbrand.fourney.utils

import com.bearbrand.fourney.model.DummyHistoryModel
import com.bearbrand.fourney.model.DummyObjectModel
import com.bearbrand.fourney.model.HistoryModel

object DummyHistory {
    fun generateDummyHistory():ArrayList<DummyHistoryModel>{
        val listDummy = ArrayList<DummyHistoryModel>()

        listDummy.add(
            DummyHistoryModel(
                "Jatim Park",
                "30 Desember 2020",
                20,
                200,
                2,
                3
            )
        )

        listDummy.add(
            DummyHistoryModel(
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
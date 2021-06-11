package com.bearbrand.fourney.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bearbrand.fourney.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HistoryViewModel : ViewModel() {

    suspend fun getHistory(uid: String): ArrayList<HistoryEntity> {
        val listHistoryEntity = ArrayList<HistoryEntity>()
        val listHistory = ArrayList<HistoryModel>()
        val gettingData = CoroutineScope(Dispatchers.IO).launch {
            val historySnapshot = Firebase.firestore.collection("history")
                .whereEqualTo("idUser", uid)
                .get()
                .await()
            for (history in historySnapshot.documents) {
                history.toObject<HistoryModel>().also {
                    Log.d("HistorytoObject", "$history")
                    listHistory.add(it!!)
                }

            }
        }
        gettingData.join()
        listHistory.forEach {
            it.place?.forEach { place ->
                val idPlace = place["idPlace"]
                val date: String = place["timeStart"] as String
                val idObject = place["idObject"]
                val arrObject = ArrayList<String>()
                if (idObject != null) {
                    for (item in idObject as ArrayList<String>) {
                        arrObject.add(item)
                    }
                }
                val placeEntity = getPlace(idPlace as String)
                val listDoneObject = getDoneObject(idPlace, arrObject)
                val listAllObject = getAllPlaceObject(idPlace)
                listHistoryEntity.add(
                    HistoryEntity(
                        placeEntity.title,
                        date,
                        listDoneObject.third,
                        listDoneObject.second,
                        listDoneObject.first.size,
                        placeEntity.numObject,
                        listAllObject,
                        arrObject,
                    )
                )
            }
        }
        return listHistoryEntity
    }

    private suspend fun getPlace(placeId: String): Place {
        var place = Place()
        val gettingData = CoroutineScope(Dispatchers.IO).launch {
            val getPlaceSnapshot = Firebase.firestore.collection("place")
                .document(placeId)
                .get()
                .await()

            getPlaceSnapshot.toObject<Place>()?.also {
                place = it
            }

        }
        gettingData.join()
        return place
    }

    private suspend fun getDoneObject(
        placeId: String,
        idObject: ArrayList<String>
    ): Triple<ArrayList<ListObject>, Int, Int> {
        val listObject = ArrayList<ListObject>()
        var xp = 0
        var coin = 0
        val gettingData = CoroutineScope(Dispatchers.IO).launch {
            for (id in idObject) {
                val getPlaceSnapshot = Firebase.firestore.collection("objects")
                    .document(placeId).collection("listObjects").document(id)
                    .get()
                    .await()

                getPlaceSnapshot.toObject<ListObject>()?.let {
                    listObject.add(it)
                    xp += it.xp
                    coin += it.coint
                }
            }
        }

        gettingData.join()
        return Triple(listObject, xp, coin)
    }

    private suspend fun getAllPlaceObject(
        placeId: String,
    ): ArrayList<ListObject> {
        val listObject = ArrayList<ListObject>()
        val gettingData = CoroutineScope(Dispatchers.IO).launch {
                val getPlaceSnapshot = Firebase.firestore.collection("objects")
                    .document(placeId).collection("listObjects")
                    .get()
                    .await()

            for (item in getPlaceSnapshot.documents) {
                item.toObject<ListObject>()?.also {
                        listObject.add(it)
                    }
            }
        }
        gettingData.join()
        return listObject
    }
}
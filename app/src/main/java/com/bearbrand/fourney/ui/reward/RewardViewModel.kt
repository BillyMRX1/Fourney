package com.bearbrand.fourney.ui.reward

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bearbrand.fourney.model.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RewardViewModel : ViewModel() {

    suspend fun getUser(uid: String): UserModel {
        var user = UserModel()
        val getData = CoroutineScope(Dispatchers.IO).launch {
            delay(2000L)
            val querySnapshot = Firebase.firestore.collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .await()
            for (users in querySnapshot.documents) {
                users.toObject<UserModel>()?.also {
                    user = it
                }
            }
        }
        getData.join()
        return user
    }
    suspend fun getUserKoin(uid: String): Int {
        var user = UserModel()
        val getData = CoroutineScope(Dispatchers.IO).launch {
            val querySnapshot = Firebase.firestore.collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .await()
            for (users in querySnapshot.documents) {
                users.toObject<UserModel>()?.also {
                    user = it
                }
            }
        }
        getData.join()
        return user.point
    }

    fun getListTicket(): LiveData<ArrayList<TiketModel>> {
        val _listKupon = MutableLiveData<ArrayList<TiketModel>>()
        val listKupon = ArrayList<TiketModel>()
        CoroutineScope(Dispatchers.IO).launch {
            val querySnapshot = Firebase.firestore.collection("listKupon")
                .get()
                .await()
            for (kupon in querySnapshot.documents) {
                kupon.toObject<TiketModel>().also {
                    listKupon.add(it!!)
                }
            }
            listKupon.sortBy { it.coin }
            _listKupon.postValue(listKupon)
        }

        return _listKupon
    }

    suspend fun getUserTiket(uid: String): ArrayList<TiketModel> {
        val listKupon = ArrayList<TiketModel>()
        var kuponUser = UserKuponModel()
            val getData = CoroutineScope(Dispatchers.IO).launch {
            val kuponUserSnapshot = Firebase.firestore.collection("kuponUser")
                .whereEqualTo("uid", uid)
                .get()
                .await()
            for (kupon in kuponUserSnapshot.documents) {
                kupon.toObject<UserKuponModel>().also {
                    if (it != null) {
                        kuponUser = it
                    }
                }
            }

            kuponUser.listKupon?.let { kuponId ->
                for (i in kuponId) {
                    val querySnapshot = Firebase.firestore.collection("listKupon")
                        .whereEqualTo("kuponId", i)
                        .get()
                        .await()
                    for (kupon in querySnapshot.documents) {
                        kupon.toObject<TiketModel>().also {
                            listKupon.add(it!!)
                        }
                    }
                    listKupon.sortBy { it.coin }

                }
            }
        }
        getData.join()
        return listKupon
    }

    suspend fun getMyChallenge(uid: String): Int {
        var numberChallenge = 0
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
                numberChallenge++
            }
        }
        return numberChallenge
    }


    fun addToUserKupon(kuponId: String, uid: String, coin: Int) {
        val list = ArrayList<String>()
        list.add(kuponId)
        val historyData = UserKuponModel(uid, list)
        val ref = FirebaseFirestore.getInstance().collection("kuponUser")
        val data = ref.document(uid)
        val query = ref.whereEqualTo("uid", uid)
        query.addSnapshotListener { document, _ ->
            if (document != null) {
                if (document.size() > 0) {
                    data.update("listKupon", FieldValue.arrayUnion(kuponId))
                } else {
                    data.set(historyData)
                }

            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val querySnapshot = Firebase.firestore.collection("users")
            val userDoc = querySnapshot
                .whereEqualTo("uid", uid)
                .get()
                .await()
            for (user in userDoc) {
                val userRef = querySnapshot.document(user.id)
                var decreaseCoin = 0.0
                decreaseCoin -= coin
                userRef.update("point", FieldValue.increment(decreaseCoin))
            }
        }
    }
}
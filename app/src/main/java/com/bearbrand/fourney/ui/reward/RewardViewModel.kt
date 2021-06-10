package com.bearbrand.fourney.ui.reward

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.model.UserModel
import com.bearbrand.fourney.utils.DummyTiket
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RewardViewModel : ViewModel() {

    fun getUser(uid: String): LiveData<UserModel> {
        val user = MutableLiveData<UserModel>()
        CoroutineScope(Dispatchers.IO).launch {
            val querySnapshot = Firebase.firestore.collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .await()
            for (users in querySnapshot.documents) {
                users.toObject<UserModel>().also {
                    user.postValue(it)
                }
            }
        }
        return user
    }

    fun getListTicket() : LiveData<ArrayList<TiketModel>>{
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
}
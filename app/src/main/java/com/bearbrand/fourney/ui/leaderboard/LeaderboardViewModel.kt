package com.bearbrand.fourney.ui.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bearbrand.fourney.model.LeaderboardModel
import com.bearbrand.fourney.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LeaderboardViewModel : ViewModel() {



    fun getLeaderboard(uid: String): Pair<LiveData<ArrayList<LeaderboardModel>>, LiveData<LeaderboardModel>> {
        val listLeaderBoard = MutableLiveData<ArrayList<LeaderboardModel>>()
        val currentUser = MutableLiveData<LeaderboardModel>()
        val tempList = ArrayList<LeaderboardModel>()
        var user= UserModel("")
        val listUser = ArrayList<UserModel>()
        CoroutineScope(Dispatchers.IO).launch {
            val getUser = CoroutineScope(Dispatchers.IO).launch {
                val querySnapshot = Firebase.firestore.collection("users")
                    .get()
                    .await()
                for (document in querySnapshot.documents) {
                    document.toObject<UserModel>().also {
                        if (it != null) {
                            if (it.uid == uid) {
                                user = it
                                listUser.add(it)
                            } else {
                                listUser.add(it)
                            }

                        }
                    }
                }
            }
            getUser.join()

            listUser.sortByDescending { it.xp }
            var count = 1
            for (i in listUser) {
                tempList.add(
                    LeaderboardModel(count, i.avatar, i.name, i.xp)
                )
                count++
            }
            for (i in tempList){
               if( i.username == user.name){
                   currentUser.postValue(i)
               }
            }
            listLeaderBoard.postValue(tempList)
        }
        return Pair(listLeaderBoard,currentUser)
    }
}
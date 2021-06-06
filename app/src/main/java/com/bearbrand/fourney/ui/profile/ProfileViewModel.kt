package com.bearbrand.fourney.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bearbrand.fourney.model.UserModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    fun getUser(uid: String): LiveData<UserModel> {
        val user = MutableLiveData<UserModel>()
        CoroutineScope(Dispatchers.IO).launch {
            val querySnapshot = Firebase.firestore.collection("users")
                .whereEqualTo("uid",uid)
                .get()
                .await()
            for(users in querySnapshot.documents){
                users.toObject<UserModel>().also {
                    user.postValue(it)
                }
            }
        }
        return user
    }

    fun changeProfileImage(uid: String, profilePic: String) {
            val userCollection =Firebase.firestore.collection("users")
            CoroutineScope(Dispatchers.IO).launch {
                val querySnapshot = userCollection
                    .whereEqualTo("uid",uid)
                    .get()
                    .await()
                for (document in querySnapshot) {
                    Log.d("ProfileViewModel","$uid $profilePic")
                    val userRef = userCollection.document(document.id)
                    userRef.update("avatar", profilePic)
                }
            }

    }

}
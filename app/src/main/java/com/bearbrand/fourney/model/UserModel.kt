package com.bearbrand.fourney.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name: String = "",
    var uid: String = "",
    var point : Int = 0,
    var xp : Int = 0,
    val email: String = "",
    val avatar:String = "",
    val address: String = "",
    val location: String,
    val phone: String = "",
):Parcelable
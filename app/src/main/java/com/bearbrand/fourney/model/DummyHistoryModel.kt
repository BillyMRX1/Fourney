package com.bearbrand.fourney.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DummyHistoryModel(
    val idPlace : String = "",
    val date : String = "" ,
    val coin : Int = 0,
    val xp : Int=0,
    val challengeDone : Int = 0,
    val challengeNumber : Int =0
) : Parcelable {
}
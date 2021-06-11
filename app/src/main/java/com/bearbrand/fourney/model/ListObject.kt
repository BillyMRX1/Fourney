package com.bearbrand.fourney.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListObject(
    var title: String = "",
    var coint: Int = 0,
    var file: String = "",
    var desc: String = "",
    var xp: Int = 0,
    var hint: String = "",
    var id : String = ""
):Parcelable
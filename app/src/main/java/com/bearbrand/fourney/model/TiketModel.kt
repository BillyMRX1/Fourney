package com.bearbrand.fourney.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TiketModel(
    val background:String = "",
    val coin:Int = 0,
    val validUntil:String = "",
    val voucherPlace:String = "",
    val voucherTitle: String = "",


) : Parcelable
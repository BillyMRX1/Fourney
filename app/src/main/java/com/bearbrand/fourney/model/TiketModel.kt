package com.bearbrand.fourney.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TiketModel(
    val voucherTitle: String = "",
    val voucherPlace:String = "",
    val validUntil:String = "",
    val coin:Int = 0,
    val id:String = "",
    val background:String = "",
) : Parcelable{
}
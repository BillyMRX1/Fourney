package com.bearbrand.fourney.model

data class HistoryModel(
    var idUser: String = "",
    var place: MutableList<HashMap<String, Any?>>? = null
)
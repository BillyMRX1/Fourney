package com.bearbrand.fourney.model

data class Place (
    var title: String = "",
    var latLng: String = "",
    var risk: String = "",
    var numObject: Int = 0,
    var isOpen: Boolean = true,
    var image: String = "",
    var isAdvertised: Boolean = true
)
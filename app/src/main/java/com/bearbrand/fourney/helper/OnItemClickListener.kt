package com.bearbrand.fourney.helper

import com.google.firebase.firestore.DocumentSnapshot

interface OnItemClickListener {
    fun onItemClick(snapshot: DocumentSnapshot, position: Int)
}
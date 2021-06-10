package com.bearbrand.fourney.tflite

import android.graphics.Bitmap

interface IClassifier {
    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F
    )  {
        override fun toString(): String {
            return "Title = $title, Confidence = $confidence)"
        }
    }

    fun recognizeImage(bitmap: Bitmap): List<Recognition>

    fun close()
}
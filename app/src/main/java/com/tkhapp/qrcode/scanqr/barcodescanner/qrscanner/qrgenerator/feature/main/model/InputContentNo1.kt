package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model

data class InputContentNo1(
    val title: String,
    val hint: String,
    val inputType: Int,
    var isEmpty: Boolean = true,
    var inputException: String = ""
)
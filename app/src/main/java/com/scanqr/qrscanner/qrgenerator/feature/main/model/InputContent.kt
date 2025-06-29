package com.scanqr.qrscanner.qrgenerator.feature.main.model

data class InputContent(
    val title: String,
    val hint: String,
    val inputType: Int,
    var isEmpty: Boolean = true,
    var inputException: String = ""
)
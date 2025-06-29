package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language

data class LanguageModelNo1(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean = false,
    var image: Int = 0
) {
    constructor() : this("", "", false, 0)
}


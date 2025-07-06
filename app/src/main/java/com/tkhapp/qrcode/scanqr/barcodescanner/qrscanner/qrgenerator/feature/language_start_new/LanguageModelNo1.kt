package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language_start_new

data class LanguageModelNo1(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean,
    var image: Int? = null
){
    constructor() : this("", "", false, 0) {}
}
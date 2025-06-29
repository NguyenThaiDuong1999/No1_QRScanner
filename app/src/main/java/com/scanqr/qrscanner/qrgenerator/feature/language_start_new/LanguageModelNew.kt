package com.scanqr.qrscanner.qrgenerator.feature.language_start_new

data class LanguageModelNew(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean,
    var image: Int? = null
){
    constructor() : this("", "", false, 0) {}
}
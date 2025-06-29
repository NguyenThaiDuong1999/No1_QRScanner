package com.scanqr.qrscanner.qrgenerator.feature.language

data class LanguageModel(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean = false,
    var image: Int = 0
) {
    constructor() : this("", "", false, 0)
}


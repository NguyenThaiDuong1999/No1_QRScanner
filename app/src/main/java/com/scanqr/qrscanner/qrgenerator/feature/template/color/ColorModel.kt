package com.scanqr.qrscanner.qrgenerator.feature.template.color

import java.io.Serializable

data class ColorModel(
    var colorImage: String?,
    var isSelect: Boolean = false
): Serializable

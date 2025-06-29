package com.scanqr.qrscanner.qrgenerator.feature.template.background

import java.io.Serializable

data class BackgroundModel(
    var backgroundImage: Int?,
    var isSelect: Boolean = false
): Serializable

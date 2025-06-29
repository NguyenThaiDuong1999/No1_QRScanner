package com.scanqr.qrscanner.qrgenerator.feature.template.logo

import java.io.Serializable

data class LogoModel(
    var logoImage: Int?,
    var isSelect: Boolean = false
): Serializable

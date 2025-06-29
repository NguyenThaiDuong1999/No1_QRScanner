package com.scanqr.qrscanner.qrgenerator.feature.template.template_sub

import java.io.Serializable

data class TemplateModel(
    var templateImage: Int?,
    var isSelect: Boolean = false
): Serializable

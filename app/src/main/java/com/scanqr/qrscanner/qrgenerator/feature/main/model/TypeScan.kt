package com.scanqr.qrscanner.qrgenerator.feature.main.model

data class TypeScan(
    val bgColor: String,
    val image: Int,
    val name: String,
    val demo: String,
    val type: Type,
    val colorText: String = "#000000"
)

enum class Type {
    TEXT,
    LOCATION,
    CONTACT,
    URL,
    WIFI,
    PHONE,
    EMAIL,
    SMS,
    BARCODE,
    FACEBOOK,
    INSTAGRAM,
    TWITTER,
    YOUTUBE,
    WHATSAPP
}

enum class TypeUI {
    SINGLE,
    MULTIPLE,
    BARCODE
}

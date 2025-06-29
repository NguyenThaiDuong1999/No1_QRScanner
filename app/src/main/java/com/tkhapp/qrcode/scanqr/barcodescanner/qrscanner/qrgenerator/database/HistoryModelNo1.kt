package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class HistoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val codeType: String,
    val uiType: String,
    val historyType: String,
    val contentRawValue: String,
    val timeHour: String,
    val timeDate: String,
    val timeDone: String,
    var isSelectMode: Boolean = false,
    var isSelected: Boolean = false
) : Serializable

enum class HistoryType {
    SCANNED,
    CREATED
}
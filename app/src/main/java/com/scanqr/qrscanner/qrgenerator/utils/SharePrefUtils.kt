package com.scanqr.qrscanner.qrgenerator.utils

import android.content.Context
import android.content.SharedPreferences

class SharePrefUtils(context: Context) {
    private val pre: SharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pre.edit()

    var isRated
        get() = pre.getBoolean("rated", false)
        set(value) {
            editor.putBoolean("rated", value)
            editor.apply()
        }
    var isPassPermission
        get() = pre.getBoolean("pass_permission", false)
        set(value) {
            editor.putBoolean("pass_permission", value)
            editor.apply()
        }
    var countExitApp
        get() = pre.getInt("count_exit_app", 1)
        set(value) {
            editor.putInt("count_exit_app", value)
            editor.apply()
        }
    var isFirstSelectLanguage
        get() = pre.getBoolean("isFirstSelectLanguage", true)
        set(value) {
            editor.putBoolean("isFirstSelectLanguage", value)
            editor.apply()
        }
}
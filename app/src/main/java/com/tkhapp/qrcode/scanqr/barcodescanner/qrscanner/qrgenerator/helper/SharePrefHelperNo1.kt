package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper

import android.content.Context

class SharePrefHelper(context: Context) : PreferencesInterface {

    private val mPrefs = context.getSharedPreferences("scan_qr", Context.MODE_PRIVATE)

    companion object {
        private const val DEFAULT_VALUE_LONG: Long = 0
        private const val DEFAULT_VALUE_INTEGER = 0
        private const val DEFAULT_VALUE_FLOAT = 0f
        const val IS_FIRST_RUN_APP = "is_first_run_app"
    }

    override fun saveBoolean(key: String?, value: Boolean) {
        mPrefs.edit().putBoolean(key, value).apply()
    }

    override fun saveString(key: String?, value: String?) {
        mPrefs.edit().putString(key, value).apply()
    }

    override fun saveFloat(key: String?, value: Float) {
        mPrefs.edit().putFloat(key, value).apply()
    }

    override fun saveInt(key: String?, value: Int) {
        mPrefs.edit().putInt(key, value).apply()
    }

    override fun saveLong(key: String?, value: Long) {
        mPrefs.edit().putLong(key, value).apply()
    }

    override fun getBoolean(key: String?): Boolean {
        return mPrefs.getBoolean(key, false)
    }

    override fun getString(key: String?): String? {
        return mPrefs.getString(key, "")
    }

    override fun getLong(key: String?): Long {
        return mPrefs.getLong(key, DEFAULT_VALUE_LONG)
    }

    override fun getInt(key: String?): Int {
        return mPrefs.getInt(key, DEFAULT_VALUE_INTEGER)
    }

    override fun getFloat(key: String?): Float {
        return mPrefs.getFloat(key, DEFAULT_VALUE_FLOAT)
    }

    override fun remove(key: String?) {
        mPrefs.edit().remove(key).apply()
    }

    override fun contain(key: String): Boolean {
        return mPrefs.contains(key)
    }

}


interface PreferencesInterface {
    fun saveBoolean(key: String?, value: Boolean)
    fun saveString(key: String?, value: String?)
    fun saveFloat(key: String?, value: Float)
    fun saveInt(key: String?, value: Int)
    fun saveLong(key: String?, value: Long)
    fun getBoolean(key: String?): Boolean
    fun getString(key: String?): String?
    fun getLong(key: String?): Long
    fun getInt(key: String?): Int
    fun getFloat(key: String?): Float
    fun remove(key: String?)
    fun contain(key: String): Boolean
}

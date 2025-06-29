package com.scanqr.qrscanner.qrgenerator.utils

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.feature.language.LanguageModelNo1
import java.util.Locale

object SystemUtilNo1 {
    private var myLocale: Locale? = null

    // Lưu ngôn ngữ đã cài đặt
    fun saveLocale(context: Context, lang: String?) {
        setPreLanguage(context, lang)
    }

    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    fun setLocale(context: Context): Context {
        val language = getPreLanguage(context)
        val langToApply = if (language.isBlank()) Locale.getDefault().toString() else language
        Log.d("sdfsdjf", "setLocale: $langToApply")
        return changeLang(langToApply, context)
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    fun changeLang(lang: String, context: Context): Context {
        val deviceLanguageParts = when {
            lang.contains("_") -> lang.split("_")
            lang.contains("-") -> lang.split("-")
            else -> listOf(lang)
        }
        val appLanguageCode = if (deviceLanguageParts.size > 1) {
            Locale(deviceLanguageParts[0], deviceLanguageParts[1])
        } else {
            Locale(deviceLanguageParts[0])
        }

        Locale.setDefault(appLanguageCode)

        val config = Configuration(context.resources.configuration)
        config.setLocale(appLanguageCode)

        return context.createConfigurationContext(config)
    }

    fun getPreLanguage(mContext: Context?): String {
        if (mContext == null) return "en"
        val preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)
        return preferences.getString("KEY_LANGUAGE", "").toString()
    }

    fun setPreLanguage(context: Context, language: String?) {
        if (language != null && language != "") {
            val preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("KEY_LANGUAGE", language)
            editor.apply()
        }
    }

    fun listLanguage(): MutableList<LanguageModelNo1> {
        val lists = mutableListOf<LanguageModelNo1>()
        lists.add(LanguageModelNo1("Español", "es", false, R.drawable.ic_span_flag))
        lists.add(LanguageModelNo1("Français", "fr", false, R.drawable.ic_french_flag))
        lists.add(LanguageModelNo1("हिन्दी", "hi", false, R.drawable.ic_hindi_flag))
        lists.add(LanguageModelNo1("English", "en", false, R.drawable.ic_english_flag))
        lists.add(LanguageModelNo1("Português (Brazil)", "pt-rBR", false, R.drawable.ic_brazil_flag))
        lists.add(LanguageModelNo1("Português (Portu)", "pt-rPT", false, R.drawable.ic_portuguese_flag))
        lists.add(LanguageModelNo1("日本語", "ja", false, R.drawable.ic_japan_flag))
        lists.add(LanguageModelNo1("Deutsch", "de", false, R.drawable.ic_german_flag))
        lists.add(LanguageModelNo1("中文 (简体)", "zh-rCN", false, R.drawable.ic_china_flag))
        lists.add(LanguageModelNo1("中文 (繁體) ", "zh-rTW", false, R.drawable.ic_china_flag))
        lists.add(LanguageModelNo1("عربي ", "ar", false, R.drawable.ic_a_rap_flag))
        lists.add(LanguageModelNo1("বাংলা ", "bn", false, R.drawable.ic_bengali_flag))
        lists.add(LanguageModelNo1("Русский ", "ru", false, R.drawable.ic_russia_flag))
        lists.add(LanguageModelNo1("Türkçe ", "tr", false, R.drawable.ic_turkey_flag))
        lists.add(LanguageModelNo1("한국인 ", "ko", false, R.drawable.ic_korean_flag))
        lists.add(LanguageModelNo1("Indonesia", "in", false, R.drawable.ic_indo_flag))
        return lists.toMutableList()
    }

    fun haveNetworkConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        val haveConnectedWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        val haveConnectedMobile = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

        return haveConnectedWifi || haveConnectedMobile
    }
}

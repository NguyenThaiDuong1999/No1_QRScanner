package com.scanqr.qrscanner.qrgenerator.feature.language_start_new

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Activity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityLanguageStartNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.intro.IntroNo1Activity
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible
import java.util.Locale

class LanguageNo1StartNewNo1Activity : BaseNo1Activity<ActivityLanguageStartNo1Binding>(), IClickLanguageNo1 {

    private var adapter: LanguageStartNewAdapterNo1? = null
    private var model: LanguageModelNewNo1 = LanguageModelNewNo1()

    override fun onBackPressCustom() {
        finishAffinity()
    }

    override fun onClick(data: LanguageModelNewNo1) {
        model = data
        SystemUtilNo1.setLocale(this)
        binding.tvSelectLanguage.text = getLocalizedString(this, model.isoLanguage, R.string.please_select_language_to_continue)
        binding.tvTitle.text = getLocalizedString(this, model.isoLanguage, R.string.language)
        binding.ivDone.visible()
    }

    private fun setLanguageDefault(): List<LanguageModelNewNo1> {
        val lists: MutableList<LanguageModelNewNo1> = ArrayList()
        val key: String = SystemUtilNo1.getPreLanguage(this)
        val systemLang: String = Resources.getSystem().configuration.locales[0].language
        Log.d("CHECK_systemLang", "systemLang: ${systemLang} ")

        lists.add(LanguageModelNewNo1("Español", "es", false, R.drawable.ic_span_flag))
        lists.add(LanguageModelNewNo1("Français", "fr", false, R.drawable.ic_french_flag))
        lists.add(LanguageModelNewNo1("हिन्दी", "hi", false, R.drawable.ic_hindi_flag))
        lists.add(LanguageModelNewNo1("English", "en", false, R.drawable.ic_english_flag))
        lists.add(LanguageModelNewNo1("Português (Brazil)", "pt-rBR", false, R.drawable.ic_brazil_flag))
        lists.add(LanguageModelNewNo1("Português (Portu)", "pt-rPT", false, R.drawable.ic_portuguese_flag))
        lists.add(LanguageModelNewNo1("日本語", "ja", false, R.drawable.ic_japan_flag))
        lists.add(LanguageModelNewNo1("Deutsch", "de", false, R.drawable.ic_german_flag))
        lists.add(LanguageModelNewNo1("中文 (简体)", "zh-rCN", false, R.drawable.ic_china_flag))
        lists.add(LanguageModelNewNo1("中文 (繁體) ", "zh-rTW", false, R.drawable.ic_china_flag))
        lists.add(LanguageModelNewNo1("عربي ", "ar", false, R.drawable.ic_a_rap_flag))
        lists.add(LanguageModelNewNo1("বাংলা ", "bn", false, R.drawable.ic_bengali_flag))
        lists.add(LanguageModelNewNo1("Русский ", "ru", false, R.drawable.ic_russia_flag))
        lists.add(LanguageModelNewNo1("Türkçe ", "tr", false, R.drawable.ic_turkey_flag))
        lists.add(LanguageModelNewNo1("한국인 ", "ko", false, R.drawable.ic_korean_flag))
        lists.add(LanguageModelNewNo1("Indonesia", "in", false, R.drawable.ic_indo_flag))

        val systemLangModel = lists.find { it.isoLanguage.contains(systemLang) }
        systemLangModel?.let {
            lists.remove(it)
            lists.add(3, it)
        }

        return lists
    }

    private fun getLocalizedString(context: Context, languageCode: String, resId: Int): String {

        val localeParts = languageCode.split("-")
        val myLocale = if (localeParts.size > 1) {
            Locale(localeParts[0], localeParts[1])
        } else {
            Locale(languageCode)
        }
        val config = Configuration(context.resources.configuration)
        config.setLocale(myLocale)
        val localizedContext = context.createConfigurationContext(config)
        return localizedContext.resources.getString(resId)
    }

    override fun initData() {

    }

    override fun initView() {
        adapter = LanguageStartNewAdapterNo1(
            setLanguageDefault(),
            this,
        )
        binding.rcvLang.adapter = adapter
        if (adapter?.isSelectLanguage() == true) {
            binding.ivDone.visible()
        } else {
            binding.ivDone.gone()
        }
        binding.ivDone.tap {
            sharePref.isFirstSelectLanguage = false
            SystemUtilNo1.setPreLanguage(this@LanguageNo1StartNewNo1Activity, model.isoLanguage)
            SystemUtilNo1.setLocale(this)
            SystemUtilNo1.changeLang(model.isoLanguage, this@LanguageNo1StartNewNo1Activity)
            startActivity(Intent(this@LanguageNo1StartNewNo1Activity, IntroNo1Activity::class.java))
            finish()
        }
    }

    override fun getViewBinding(): ActivityLanguageStartNo1Binding {
        return ActivityLanguageStartNo1Binding.inflate(layoutInflater)
    }
}
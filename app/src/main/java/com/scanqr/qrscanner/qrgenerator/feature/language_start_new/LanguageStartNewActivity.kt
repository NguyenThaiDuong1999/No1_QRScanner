package com.scanqr.qrscanner.qrgenerator.feature.language_start_new

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityLanguageStartBinding
import com.scanqr.qrscanner.qrgenerator.feature.intro.IntroActivity
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtil
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible
import java.util.Locale

class LanguageStartNewActivity : BaseActivity<ActivityLanguageStartBinding>(), IClickLanguage {

    private var adapter: LanguageStartNewAdapter? = null
    private var model: LanguageModelNew = LanguageModelNew()

    override fun onBackPressCustom() {
        finishAffinity()
    }

    override fun onClick(data: LanguageModelNew) {
        model = data
        SystemUtil.setLocale(this)
        binding.tvSelectLanguage.text = getLocalizedString(this, model.isoLanguage, R.string.please_select_language_to_continue)
        binding.tvTitle.text = getLocalizedString(this, model.isoLanguage, R.string.language)
        binding.ivDone.visible()
    }

    private fun setLanguageDefault(): List<LanguageModelNew> {
        val lists: MutableList<LanguageModelNew> = ArrayList()
        val key: String = SystemUtil.getPreLanguage(this)
        val systemLang: String = Resources.getSystem().configuration.locales[0].language
        Log.d("CHECK_systemLang", "systemLang: ${systemLang} ")

        lists.add(LanguageModelNew("Español", "es", false, R.drawable.ic_span_flag))
        lists.add(LanguageModelNew("Français", "fr", false, R.drawable.ic_french_flag))
        lists.add(LanguageModelNew("हिन्दी", "hi", false, R.drawable.ic_hindi_flag))
        lists.add(LanguageModelNew("English", "en", false, R.drawable.ic_english_flag))
        lists.add(LanguageModelNew("Português (Brazil)", "pt-rBR", false, R.drawable.ic_brazil_flag))
        lists.add(LanguageModelNew("Português (Portu)", "pt-rPT", false, R.drawable.ic_portuguese_flag))
        lists.add(LanguageModelNew("日本語", "ja", false, R.drawable.ic_japan_flag))
        lists.add(LanguageModelNew("Deutsch", "de", false, R.drawable.ic_german_flag))
        lists.add(LanguageModelNew("中文 (简体)", "zh-rCN", false, R.drawable.ic_china_flag))
        lists.add(LanguageModelNew("中文 (繁體) ", "zh-rTW", false, R.drawable.ic_china_flag))
        lists.add(LanguageModelNew("عربي ", "ar", false, R.drawable.ic_a_rap_flag))
        lists.add(LanguageModelNew("বাংলা ", "bn", false, R.drawable.ic_bengali_flag))
        lists.add(LanguageModelNew("Русский ", "ru", false, R.drawable.ic_russia_flag))
        lists.add(LanguageModelNew("Türkçe ", "tr", false, R.drawable.ic_turkey_flag))
        lists.add(LanguageModelNew("한국인 ", "ko", false, R.drawable.ic_korean_flag))
        lists.add(LanguageModelNew("Indonesia", "in", false, R.drawable.ic_indo_flag))

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
        adapter = LanguageStartNewAdapter(
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
            SystemUtil.setPreLanguage(this@LanguageStartNewActivity, model.isoLanguage)
            SystemUtil.setLocale(this)
            SystemUtil.changeLang(model.isoLanguage, this@LanguageStartNewActivity)
            startActivity(Intent(this@LanguageStartNewActivity, IntroActivity::class.java))
            finish()
        }
    }

    override fun getViewBinding(): ActivityLanguageStartBinding {
        return ActivityLanguageStartBinding.inflate(layoutInflater)
    }
}
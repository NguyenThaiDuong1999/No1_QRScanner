package com.scanqr.qrscanner.qrgenerator.feature.language

import android.content.Intent
import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Activity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityLanguageNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.scanqr.qrscanner.qrgenerator.utils.invisible
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible
import java.util.Locale

class LanguageNo1Activity : BaseNo1Activity<ActivityLanguageNo1Binding>() {
    private var adapter: LanguageAdapterNo1? = null
    private var codeLang = ""
    private var listLanguage: ArrayList<LanguageModelNo1> = arrayListOf()
    override fun getViewBinding(): ActivityLanguageNo1Binding {
        return ActivityLanguageNo1Binding.inflate(layoutInflater)
    }

    override fun onBackPressCustom() {
        finish()
    }

    override fun initView() {
        setCodeLanguage()
        adapter = LanguageAdapterNo1(SystemUtilNo1.listLanguage()) {
            binding.ivDone.visible()
            codeLang = it.isoLanguage
        }
        binding.rcvLang.adapter = adapter
        binding.icBack.tap {
            finish()
        }
        binding.ivDone.tap {
            SystemUtilNo1.saveLocale(baseContext, codeLang)
            startActivity(Intent(this@LanguageNo1Activity, MainNo1Activity::class.java))
            finishAffinity()
        }
        initLang()
        binding.ivDone.invisible()
    }

    override fun initData() {

    }

    private fun setCodeLanguage() {
        val codeLangDefault = Locale.getDefault().language
        val langDefault = arrayOf("fr", "pt", "es", "de", "hi", "in", "en")
        codeLang =
            if (SystemUtilNo1.getPreLanguage(this) == "")
                if (!mutableListOf(*langDefault).contains(codeLangDefault)) "en"
                else codeLangDefault
            else {
                SystemUtilNo1.getPreLanguage(this)
            }
    }

    private fun initLang() {
        var pos = 0
        listLanguage.clear()
        listLanguage.addAll(SystemUtilNo1.listLanguage())
        listLanguage.forEachIndexed { index, languageModel ->
            if (languageModel.isoLanguage == codeLang) {
                pos = index
                return@forEachIndexed
            }
        }
        val temp = listLanguage[pos]
        temp.isCheck = true
        listLanguage.removeAt(pos)
        listLanguage.add(0, temp)
        adapter?.addList(listLanguage)
    }
}
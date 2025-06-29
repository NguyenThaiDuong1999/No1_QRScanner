package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language_start

import android.content.Intent
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityLanguageStartNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.intro.IntroNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language.LanguageAdapterNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language.LanguageModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import java.util.Locale

class LanguageStartNo1Activity : BaseNo1Activity<ActivityLanguageStartNo1Binding>() {


    private var codeLang = ""
    private var selectLang = ""
    private var adapter: LanguageAdapterNo1? = null
    private var listLanguage: ArrayList<LanguageModelNo1> = arrayListOf()

    override fun initView() {
        setCodeLanguage()
        adapter = LanguageAdapterNo1(SystemUtilNo1.listLanguage()) {
            codeLang = it.isoLanguage
            selectLang = it.languageName
            adapter?.setActive(it.isoLanguage)
        }
        binding.rcvLang.adapter = adapter
        binding.ivDone.tap {
            SystemUtilNo1.saveLocale(baseContext, codeLang)
            nextAction()
        }
        initLang()
    }


    private fun nextAction() {
        startActivity(Intent(this@LanguageStartNo1Activity, IntroNo1Activity::class.java))
        finish()
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finishAffinity()
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


    override fun getViewBinding(): ActivityLanguageStartNo1Binding {
        return ActivityLanguageStartNo1Binding.inflate(layoutInflater)
    }
}
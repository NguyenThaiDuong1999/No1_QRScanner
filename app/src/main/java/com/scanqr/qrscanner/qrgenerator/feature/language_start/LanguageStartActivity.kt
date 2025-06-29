package com.scanqr.qrscanner.qrgenerator.feature.language_start

import android.content.Intent
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityLanguageStartBinding
import com.scanqr.qrscanner.qrgenerator.feature.intro.IntroActivity
import com.scanqr.qrscanner.qrgenerator.feature.language.LanguageAdapter
import com.scanqr.qrscanner.qrgenerator.feature.language.LanguageModel
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtil
import com.scanqr.qrscanner.qrgenerator.utils.tap
import java.util.Locale

class LanguageStartActivity : BaseActivity<ActivityLanguageStartBinding>() {


    private var codeLang = ""
    private var selectLang = ""
    private var adapter: LanguageAdapter? = null
    private var listLanguage: ArrayList<LanguageModel> = arrayListOf()

    override fun initView() {
        setCodeLanguage()
        adapter = LanguageAdapter(SystemUtil.listLanguage()) {
            codeLang = it.isoLanguage
            selectLang = it.languageName
            adapter?.setActive(it.isoLanguage)
        }
        binding.rcvLang.adapter = adapter
        binding.ivDone.tap {
            SystemUtil.saveLocale(baseContext, codeLang)
            nextAction()
        }
        initLang()
    }


    private fun nextAction() {
        startActivity(Intent(this@LanguageStartActivity, IntroActivity::class.java))
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
            if (SystemUtil.getPreLanguage(this) == "")
                if (!mutableListOf(*langDefault).contains(codeLangDefault)) "en"
                else codeLangDefault
            else {
                SystemUtil.getPreLanguage(this)
            }
    }

    private fun initLang() {
        var pos = 0
        listLanguage.clear()
        listLanguage.addAll(SystemUtil.listLanguage())
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


    override fun getViewBinding(): ActivityLanguageStartBinding {
        return ActivityLanguageStartBinding.inflate(layoutInflater)
    }
}
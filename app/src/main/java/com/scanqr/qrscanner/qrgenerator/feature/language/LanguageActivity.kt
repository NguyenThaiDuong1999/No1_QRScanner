package com.scanqr.qrscanner.qrgenerator.feature.language

import android.content.Intent
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityLanguageBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainActivity
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtil
import com.scanqr.qrscanner.qrgenerator.utils.invisible
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible
import java.util.Locale

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private var adapter: LanguageAdapter? = null
    private var codeLang = ""
    private var listLanguage: ArrayList<LanguageModel> = arrayListOf()
    override fun getViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun onBackPressCustom() {
        finish()
    }

    override fun initView() {
        setCodeLanguage()
        adapter = LanguageAdapter(SystemUtil.listLanguage()) {
            binding.ivDone.visible()
            codeLang = it.isoLanguage
        }
        binding.rcvLang.adapter = adapter
        binding.icBack.tap {
            finish()
        }
        binding.ivDone.tap {
            SystemUtil.saveLocale(baseContext, codeLang)
            startActivity(Intent(this@LanguageActivity, MainActivity::class.java))
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
}
package com.scanqr.qrscanner.qrgenerator.feature.setting

import android.annotation.SuppressLint
import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Activity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityPolicyNo1Binding
import com.scanqr.qrscanner.qrgenerator.utils.tap

class PolicyNo1Activity : BaseNo1Activity<ActivityPolicyNo1Binding>() {

    override fun getViewBinding(): ActivityPolicyNo1Binding {
        return ActivityPolicyNo1Binding.inflate(layoutInflater)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        binding.ivBack.tap {
            finish()
        }
        binding.viewPolicy.settings.javaScriptEnabled = true
        binding.viewPolicy.loadUrl("https://amazic.net/Privacy-Policy-Qrscan.html")
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finish()
    }

}
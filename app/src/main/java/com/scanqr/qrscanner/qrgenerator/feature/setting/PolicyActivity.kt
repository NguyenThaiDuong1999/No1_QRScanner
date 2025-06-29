package com.scanqr.qrscanner.qrgenerator.feature.setting

import android.annotation.SuppressLint
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityPolicyBinding
import com.scanqr.qrscanner.qrgenerator.utils.tap

class PolicyActivity : BaseActivity<ActivityPolicyBinding>() {

    override fun getViewBinding(): ActivityPolicyBinding {
        return ActivityPolicyBinding.inflate(layoutInflater)
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
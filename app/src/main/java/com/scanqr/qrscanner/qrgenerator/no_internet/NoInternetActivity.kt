package com.scanqr.qrscanner.qrgenerator.no_internet

import androidx.activity.OnBackPressedCallback
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityNoInternetBinding
import com.scanqr.qrscanner.qrgenerator.utils.openWifiSetting
import com.scanqr.qrscanner.qrgenerator.utils.tap


class NoInternetActivity : BaseActivity<ActivityNoInternetBinding>() {

    override fun getViewBinding(): ActivityNoInternetBinding {
        return ActivityNoInternetBinding.inflate(layoutInflater)
    }

    override fun initView() {

        binding.tvTryAgain.tap {
            openWifiSetting()
        }
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finishAffinity()
    }

}
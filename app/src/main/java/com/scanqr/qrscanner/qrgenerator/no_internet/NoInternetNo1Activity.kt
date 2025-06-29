package com.scanqr.qrscanner.qrgenerator.no_internet

import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Activity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityNoInternetNo1Binding
import com.scanqr.qrscanner.qrgenerator.utils.openWifiSetting
import com.scanqr.qrscanner.qrgenerator.utils.tap


class NoInternetNo1Activity : BaseNo1Activity<ActivityNoInternetNo1Binding>() {

    override fun getViewBinding(): ActivityNoInternetNo1Binding {
        return ActivityNoInternetNo1Binding.inflate(layoutInflater)
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
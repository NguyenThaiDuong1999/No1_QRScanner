package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.no_internet

import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityNoInternetNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.openWifiSetting
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap


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
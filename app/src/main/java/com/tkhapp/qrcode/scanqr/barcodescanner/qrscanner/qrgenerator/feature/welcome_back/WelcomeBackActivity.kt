package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.welcome_back

import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.amazic.library.ads.callback.AppOpenCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityWelcomeBackBinding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap

class WelcomeBackActivity : BaseNo1Activity<ActivityWelcomeBackBinding>() {

    override fun initView() {
        binding.tvStart.tap {
            finish()
        }

        loadNative(
            binding.frAds,
            Constants.RemoteKeys.native_wb,
            Constants.RemoteKeys.native_wb,
            Constants.RemoteKeys.native_backup,
            R.layout.native_large_ads_with_button_bellow,
            R.layout.shimmer_native_large_with_button_bellow
        )
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {

    }

    override fun getViewBinding(): ActivityWelcomeBackBinding =
        ActivityWelcomeBackBinding.inflate(layoutInflater)
}

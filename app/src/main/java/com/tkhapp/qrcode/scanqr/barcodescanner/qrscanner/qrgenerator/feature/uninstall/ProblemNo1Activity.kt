package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.uninstall

import android.os.Bundle
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityProblemNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.no_internet.NoInternetNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.SCREEN
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.SPLASH_ACTIVITY
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.launchActivity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap

class ProblemNo1Activity : BaseNo1Activity<ActivityProblemNo1Binding>() {

    override fun initView() {
        loadBannerAds(binding.frAdsBanner, Constants.RemoteKeys.banner_setting)
        binding.apply {
            listOf(tvExplore, tvTryAgain, noUninstall, ivBack).forEach {
                it.tap {
                    if (SystemUtilNo1.haveNetworkConnection(this@ProblemNo1Activity)) {
                        launchActivity(MainNo1Activity::class.java)
                        finishAffinity()
                    } else {
                        launchActivity(Bundle().apply {
                            putString(SCREEN, SPLASH_ACTIVITY)
                        }, NoInternetNo1Activity::class.java)
                    }
                }
            }
            stillUninstall.tap {
                launchActivity(UninstallNo1Activity::class.java)
            }
        }
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        launchActivity(MainNo1Activity::class.java)
        finishAffinity()
    }

    override fun getViewBinding(): ActivityProblemNo1Binding = ActivityProblemNo1Binding.inflate(layoutInflater)

}
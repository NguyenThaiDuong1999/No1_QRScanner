package com.scanqr.qrscanner.qrgenerator.feature.uninstall

import android.os.Bundle
import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Activity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityProblemNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.scanqr.qrscanner.qrgenerator.no_internet.NoInternetNo1Activity
import com.scanqr.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.SCREEN
import com.scanqr.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.SPLASH_ACTIVITY
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.scanqr.qrscanner.qrgenerator.utils.launchActivity
import com.scanqr.qrscanner.qrgenerator.utils.tap

class ProblemNo1Activity : BaseNo1Activity<ActivityProblemNo1Binding>() {

    override fun initView() {
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
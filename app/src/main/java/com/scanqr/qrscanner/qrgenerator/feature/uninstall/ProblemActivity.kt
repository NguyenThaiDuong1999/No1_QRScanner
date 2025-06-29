package com.scanqr.qrscanner.qrgenerator.feature.uninstall

import android.annotation.SuppressLint
import android.os.Bundle
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityProblemBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainActivity
import com.scanqr.qrscanner.qrgenerator.no_internet.NoInternetActivity
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.SCREEN
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.SPLASH_ACTIVITY
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtil
import com.scanqr.qrscanner.qrgenerator.utils.launchActivity
import com.scanqr.qrscanner.qrgenerator.utils.tap

class ProblemActivity : BaseActivity<ActivityProblemBinding>() {

    override fun initView() {
        binding.apply {
            listOf(tvExplore, tvTryAgain, noUninstall, ivBack).forEach {
                it.tap {
                    if (SystemUtil.haveNetworkConnection(this@ProblemActivity)) {
                        launchActivity(MainActivity::class.java)
                        finishAffinity()
                    } else {
                        launchActivity(Bundle().apply {
                            putString(SCREEN, SPLASH_ACTIVITY)
                        }, NoInternetActivity::class.java)
                    }
                }
            }
            stillUninstall.tap {
                launchActivity(UninstallActivity::class.java)
            }
        }
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        launchActivity(MainActivity::class.java)
        finishAffinity()
    }

    override fun getViewBinding(): ActivityProblemBinding = ActivityProblemBinding.inflate(layoutInflater)

}
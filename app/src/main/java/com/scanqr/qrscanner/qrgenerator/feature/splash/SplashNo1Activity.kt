package com.scanqr.qrscanner.qrgenerator.feature.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Activity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivitySplashNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.language_start_new.LanguageNo1StartNewNo1Activity
import com.scanqr.qrscanner.qrgenerator.feature.uninstall.ProblemNo1Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashNo1Activity : BaseNo1Activity<ActivitySplashNo1Binding>() {
    private var countDownTimer: CountDownTimer? = null

    override fun getViewBinding(): ActivitySplashNo1Binding = ActivitySplashNo1Binding.inflate(layoutInflater)


    override fun initView() {
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action.equals(Intent.ACTION_MAIN)
        ) {
            finish()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if (Intent.ACTION_VIEW == intent.action) {
                //From app shortcut
                launchActivityIntent(Intent(this@SplashNo1Activity, ProblemNo1Activity::class.java))
                finish()
            } else {
                //From app launch
                if (isActive && arrayOf(Lifecycle.State.RESUMED, Lifecycle.State.STARTED).contains(ProcessLifecycleOwner.get().lifecycle.currentState))
                    startNextScreen()
            }
        }
        var count = 0
        countDownTimer = object : CountDownTimer(2000, 25) {
            @SuppressLint("SetTextI18n")
            override fun onTick(p0: Long) {
                count++
                if (count < 100) {
                    binding.progressIndicator.progress = count
                    binding.tvProgress.text = getString(R.string.loading) + " ($count%)..."
                }
            }

            override fun onFinish() {
            }
        }
        countDownTimer?.start()
    }

    override fun initData() {
    }

    override fun onBackPressCustom() {

    }

    @SuppressLint("SetTextI18n")
    private fun startNextScreen() {
        binding.progressIndicator.progress = 100
        binding.tvProgress.text = getString(R.string.loading) + " (100%)..."
        launchActivityIntent(Intent(this@SplashNo1Activity, LanguageNo1StartNewNo1Activity::class.java))
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        startNextScreen()
    }
}


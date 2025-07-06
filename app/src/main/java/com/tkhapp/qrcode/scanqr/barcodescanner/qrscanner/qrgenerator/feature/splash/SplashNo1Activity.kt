package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.amazic.library.Utils.EventTrackingHelper
import com.amazic.library.Utils.EventTrackingHelper.native_language
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.amazic.library.ads.callback.AppOpenCallback
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.callback.NativeCallback
import com.amazic.library.ads.splash_ads.AsyncSplash
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivitySplashNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language_start_new.LanguageStartNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.uninstall.ProblemNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.welcome_back.WelcomeBackActivity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants

@SuppressLint("CustomSplashScreen")
class SplashNo1Activity : BaseNo1Activity<ActivitySplashNo1Binding>() {
    private var tAG = "SplashNo1Activity"
    private var countDownTimer: CountDownTimer? = null
    private var countOpenSplash = 1L
    private var jsonIdAdsDefault = ""

    companion object {
        var isShowSplashAds = false
        var isCloseSplashAds = false
        var nativeLanguagePreload: NativeAd? = null
        var isShowNativeLanguagePreloadAtSplash = false
    }

    private val openCallback = object : AppOpenCallback() {
        override fun onNextAction() {
            super.onNextAction()
            startNextAct()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            isShowSplashAds = true
        }

        override fun onAdImpression() {
            super.onAdImpression()
            AppOpenManager.getInstance().appOpenAdSplash = null
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            isCloseSplashAds = true
        }

        override fun onAdFailedToShowFullScreenContent() {
            super.onAdFailedToShowFullScreenContent()
            isCloseSplashAds = true
        }
    }

    private val interCallback = object : InterCallback() {
        override fun onAdLoaded(interstitialAd: InterstitialAd?) {
            super.onAdLoaded(interstitialAd)
            interstitialAd?.setImmersiveMode(false)
        }

        override fun onNextAction() {
            super.onNextAction()
            startNextAct()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            isShowSplashAds = true
        }

        override fun onAdImpression() {
            super.onAdImpression()
            Admob.getInstance().interstitialAdSplash = null
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            isCloseSplashAds = true
        }

        override fun onAdFailedToShowFullScreenContent() {
            super.onAdFailedToShowFullScreenContent()
            isCloseSplashAds = true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startNextAct() {
        binding.progressIndicator.progress = 100
        binding.tvProgress.text = getString(R.string.loading) + " (100%)..."
        countDownTimer?.cancel()
        if (Intent.ACTION_VIEW == intent.action) {
            //From app shortcut
            launchActivityIntent(Intent(this@SplashNo1Activity, ProblemNo1Activity::class.java))
            finish()
        } else {
            //From app launch
            launchActivityIntent(
                Intent(
                    this@SplashNo1Activity,
                    LanguageStartNo1Activity::class.java
                )
            )
            finish()
        }
    }

    override fun getViewBinding(): ActivitySplashNo1Binding =
        ActivitySplashNo1Binding.inflate(layoutInflater)

    override fun initView() {
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action.equals(Intent.ACTION_MAIN)
        ) {
            finish()
            return
        }

        //to log event time splash check in lib
        AsyncSplash.getInstance().setTimeSplashCheck()

        //to log event language view
        isShowSplashAds = false
        isCloseSplashAds = false
        //end

        EventTrackingHelper.logEvent(this, Constants.TrackingKeys.splash_open)
        countOpenSplash =
            RemoteConfigHelper.getInstance().get_config_long(this, "countOpenSplash") + 1
        if (countOpenSplash <= 10) {
            Log.d(tAG, "logEvent: $countOpenSplash")
            EventTrackingHelper.logEvent(
                this,
                Constants.TrackingKeys.splash_open + "_${countOpenSplash}"
            )
        }
        RemoteConfigHelper.getInstance()
            .set_config_long_commit(this, "countOpenSplash", countOpenSplash)
        RemoteConfigHelper.getInstance().set_config_commit(this, "isLogEventLanguageFO", false)

        handleAsyncSplash()
        handleProgress()
    }

    private fun handleAsyncSplash() {
        AsyncSplash.getInstance().init(
            activity = this@SplashNo1Activity,
            appOpenCallback = openCallback,
            interCallback = interCallback,
            adjustKey = "",
            linkServer = getString(R.string.link_server),
            appId = getString(R.string.app_id),
            jsonIdAdsDefault = jsonIdAdsDefault
        )
        AsyncSplash.getInstance().setDebug(false)
        AsyncSplash.getInstance().setAsyncSplashAds()
        //AsyncSplash.getInstance().setUseIdAdsFromRemoteConfig("id_ads")
        AsyncSplash.getInstance().setListTurnOffRemoteKeys(addListTurnOffRemoteKeys())
        AsyncSplash.getInstance().setInitWelcomeBackBelowResumeAds(WelcomeBackActivity::class.java)
        AsyncSplash.getInstance().handleAsync(
            this@SplashNo1Activity, this@SplashNo1Activity, lifecycleScope,
            onAsyncSplashDone = {
                preloadANativeMainLanguage()
            },
            onNoInternetAction = {
                interCallback.onNextAction()
            }
        )
    }

    private fun preloadANativeMainLanguage() {
        Admob.getInstance().loadNativeAds(
            this,
            AdmobApi.getInstance().getListIDByName(native_language),
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                    super.onNativeAdLoaded(nativeAd)
                    nativeLanguagePreload = nativeAd
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    isShowNativeLanguagePreloadAtSplash = true
                }
            }, native_language
        )
    }

    private fun addListTurnOffRemoteKeys(): MutableList<String> {
        return mutableListOf(
            Constants.RemoteKeys.banner_setting,
            Constants.RemoteKeys.inter_home,
            Constants.RemoteKeys.inter_create,
            Constants.RemoteKeys.inter_history,
            Constants.RemoteKeys.collapse_home,
            Constants.RemoteKeys.collapse_scan,
            Constants.RemoteKeys.collapse_banner_setting,
            Constants.RemoteKeys.collapse_result,
            Constants.RemoteKeys.native_result,
            Constants.RemoteKeys.collapse_template,
            Constants.RemoteKeys.native_create,
            Constants.RemoteKeys.native_intro_full,
            Constants.RemoteKeys.native_intro_full_2,
            Constants.RemoteKeys.native_intro_full1,
            Constants.RemoteKeys.native_intro_full1_2,
            Constants.RemoteKeys.native_permission,
            Constants.RemoteKeys.native_intro,
            Constants.RemoteKeys.native_language,
            Constants.RemoteKeys.native_wb,
            Constants.RemoteKeys.resume_wb,
            Constants.RemoteKeys.native_backup
        )
    }

    private fun handleProgress() {
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
}


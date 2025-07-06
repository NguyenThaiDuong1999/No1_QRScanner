package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.intro

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.amazic.library.Utils.EventTrackingHelper
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.Admob
import com.amazic.library.organic.TechManager
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityIntroNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language_start_new.LanguageStartNo1Activity.Companion.isShowNativeIntroPreloadAtSplash
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language_start_new.LanguageStartNo1Activity.Companion.nativeIntroPreload
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.permission.PermissionNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper.SharePrefHelper
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_SETTING_CONTINUE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class IntroNo1Activity : BaseNo1Activity<ActivityIntroNo1Binding>() {
    private val TAG = "IntroNo1Activity"
    private var introAdapter: IntroAdapterNo1? = null
    private var listData = mutableListOf<IntroModelNo1>()
    private var isFirst = true
    private var countOpenSplash = 1L
    private var isPause = false

    companion object {
        var isLogEventOnboardOpen = false
        var nativeAdFullScreen: NativeAd? = null
        var nativeAdFullScreen2: NativeAd? = null
        var nativeAdFullScreen1: NativeAd? = null
        var nativeAdFullScreen12: NativeAd? = null
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        isPause = false
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isPause) {
                showNativeIntroPreloadAtSplash()
            }
        }, 1000)
    }

    override fun onPause() {
        super.onPause()
        isPause = true
        Log.d(TAG, "onPause: ")
    }

    private fun showNativeIntroPreloadAtSplash() {
        if (nativeIntroPreload != null && !isShowNativeIntroPreloadAtSplash && !TechManager.getInstance().isTech(this) && Admob.getInstance().showAllAds) {
            val adView: NativeAdView = layoutInflater.inflate(
                R.layout.native_small_ads_with_button_above,
                binding.frAds,
                false
            ) as NativeAdView
            binding.frAds.addView(adView)
            Admob.getInstance().populateNativeAdView(nativeIntroPreload, adView)
        }
    }


    override fun initView() {
        EventTrackingHelper.logEvent(this@IntroNo1Activity, "onboard_open")
        RemoteConfigHelper.getInstance().set_config_long(
            this@IntroNo1Activity,
            "onboard_open",
            RemoteConfigHelper.getInstance()
                .get_config_long(this@IntroNo1Activity, "onboard_open") + 1
        )
        loadNativeIntro()

        countOpenSplash = RemoteConfigHelper.getInstance().get_config_long(this, "countOpenSplash")
        if (!isLogEventOnboardOpen) {
            if (countOpenSplash <= 10) {
                EventTrackingHelper.logEvent(this, "onboard_open" + "_${countOpenSplash}")
            }
            isLogEventOnboardOpen = true
        }

        val data = getListIntro()
        binding.vpgSlideIntro.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (isFirst) {
                    isFirst = false
                    return
                }
                addBottomDots(position)
                if (listData[position].image == 99) {
                    binding.frAds.gone()
                    binding.rlButton.gone()
                } else {
                    binding.frAds.visible()
                    binding.rlButton.visible()
                }
                //log event
                when (listData[position].type) {
                    "guide1" -> {
                        EventTrackingHelper.logEvent(this@IntroNo1Activity, "Onboarding_1_view")
                    }

                    "guide2" -> {
                        EventTrackingHelper.logEvent(this@IntroNo1Activity, "Onboarding_2_view")
                    }

                    "guide3" -> {
                        EventTrackingHelper.logEvent(this@IntroNo1Activity, "Onboarding_3_view")
                    }

                    "guide4" -> {
                        EventTrackingHelper.logEvent(this@IntroNo1Activity, "Onboarding_4_view")
                    }

                    "ads" -> {
                        introAdapter?.notifyNativeAdFullScreen()
                        EventTrackingHelper.logEvent(this@IntroNo1Activity, "Native_full_view")
                    }

                    "ads1" -> {
                        introAdapter?.notifyNativeAdFullScreen1()
                        EventTrackingHelper.logEvent(this@IntroNo1Activity, "Native_full1_view")
                    }
                }
            }
        })
        introAdapter = IntroAdapterNo1(this, data)
        binding.vpgSlideIntro.adapter = introAdapter
        binding.tvNextRight.tap {
            onClickNext()
        }
    }

    private fun loadNativeIntro() {
        loadNative(
            binding.frAds,
            Constants.RemoteKeys.native_intro,
            Constants.RemoteKeys.native_intro,
            Constants.RemoteKeys.native_backup,
            R.layout.native_small_ads_with_button_bellow,
            R.layout.shimmer_native_small_with_button_bellow
        )
    }

    private fun onClickNext() {
        if (binding.vpgSlideIntro.currentItem < listData.size - 1) {
            binding.vpgSlideIntro.currentItem += 1
        } else {
            startNextAct()
        }
    }

    private fun startNextAct() {
        if (SharePrefHelper(this).getBoolean(IS_SETTING_CONTINUE)) {
            startActivity(Intent(this, MainNo1Activity::class.java))
            finish()
        } else {
            startActivity(Intent(this, PermissionNo1Activity::class.java))
            finish()
        }
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finishAffinity()
    }

    private fun addBottomDots(currentPage: Int) {
        binding.linearDots.removeAllViews()
        val dots = arrayOfNulls<ImageView>(listData.size)
        for (i in 0 until listData.size) {
            dots[i] = ImageView(this)
            if (i == currentPage)
                dots[i]!!.setImageResource(R.drawable.ic_intro_selected)
            else
                dots[i]!!.setImageResource(R.drawable.ic_intro_unselected)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.linearDots.addView(dots[i], params)
        }
    }


    private fun getListIntro(): MutableList<IntroModelNo1> {
        listData.add(
            IntroModelNo1(
                R.drawable.img_intro_1,
                getString(R.string.intro_desc_1),
                getString(R.string.intro_content_1),
                "guide1"
            )
        )
        if (Admob.getInstance().checkCondition(this, Constants.RemoteKeys.native_intro_full)) {
            listData.add(IntroModelNo1(99, "ads", "ads", "ads"))
        }
        listData.add(
            IntroModelNo1(
                R.drawable.img_intro_2,
                getString(R.string.intro_desc_2),
                getString(R.string.intro_content_2),
                "guide2"
            )
        )
        if (Admob.getInstance().checkCondition(this, Constants.RemoteKeys.native_intro_full1)) {
            listData.add(IntroModelNo1(99, "ads1", "ads1", "ads1"))
        }
        listData.add(
            IntroModelNo1(
                R.drawable.img_intro_3,
                getString(R.string.intro_desc_3),
                getString(R.string.intro_content_3),
                "guide3"
            )
        )
        listData.add(
            IntroModelNo1(
                R.drawable.img_intro_4,
                getString(R.string.customize_your_qr_code),
                getString(R.string.personalize_your_qr_codes_with_colors_logos_and_styles),
                "guide4"
            )
        )
        addBottomDots(0)
        return listData
    }

    override fun getViewBinding(): ActivityIntroNo1Binding {
        return ActivityIntroNo1Binding.inflate(layoutInflater)
    }

}
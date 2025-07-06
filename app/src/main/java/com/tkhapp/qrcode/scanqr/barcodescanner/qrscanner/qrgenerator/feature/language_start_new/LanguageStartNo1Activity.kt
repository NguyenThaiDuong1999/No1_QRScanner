package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language_start_new

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.amazic.library.Utils.EventTrackingHelper
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.Utils.RemoteConfigHelper.inter_splash
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.callback.NativeCallback
import com.amazic.library.organic.TechManager
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityLanguageStartNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.intro.IntroNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.splash.SplashNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.splash.SplashNo1Activity.Companion.isShowNativeLanguagePreloadAtSplash
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.splash.SplashNo1Activity.Companion.nativeLanguagePreload
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.RemoteKeys.native_intro
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible
import java.util.Locale

class LanguageStartNo1Activity : BaseNo1Activity<ActivityLanguageStartNo1Binding>(), IClickLanguageNo1 {
    private val TAG = "LanguageStartActivity"
    private var adapter: LanguageStartAdapterNo1? = null
    private var model: LanguageModelNo1 = LanguageModelNo1()
    private var countOpenSplash = 1L
    private var isPause = false
    private var isChooseLanguage = false

    companion object {
        var isLogEventLanguageUserView = false
        var nativeIntroPreload: NativeAd? = null
        var isShowNativeIntroPreloadAtSplash = false
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        isPause = false
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isPause) {
                showNativeLanguagePreloadAtSplash()
            }
            Log.d(TAG, "isShowSplashAds: ${SplashNo1Activity.isShowSplashAds} - isCloseSplashAds: ${SplashNo1Activity.isCloseSplashAds}")
            if (SplashNo1Activity.isShowSplashAds) {
                if (SplashNo1Activity.isCloseSplashAds) {
                    if (!isLogEventLanguageUserView && !isPause) {
                        EventTrackingHelper.logEvent(this@LanguageStartNo1Activity, "language_user_view")
                        if (countOpenSplash <= 10) {
                            Log.d(TAG, "logEventOnResume: $countOpenSplash")
                            EventTrackingHelper.logEvent(this@LanguageStartNo1Activity, "language_user_view" + "_${countOpenSplash}")
                            isLogEventLanguageUserView = true
                        }
                    }
                }
            } else {
                if (!isLogEventLanguageUserView && !isPause) {
                    EventTrackingHelper.logEvent(this@LanguageStartNo1Activity, "language_user_view")
                    if (countOpenSplash <= 10) {
                        Log.d(TAG, "logEventOnResume: $countOpenSplash")
                        EventTrackingHelper.logEvent(this@LanguageStartNo1Activity, "language_user_view" + "_${countOpenSplash}")
                        isLogEventLanguageUserView = true
                    }
                }
            }
        }, 1000)
    }

    override fun onPause() {
        super.onPause()
        isPause = true
        Log.d(TAG, "onPause: ")
    }

    private fun showNativeLanguagePreloadAtSplash() {
        if (nativeLanguagePreload != null && !isShowNativeLanguagePreloadAtSplash) {
            val adView: NativeAdView = layoutInflater.inflate(R.layout.native_language_custom, binding.frAds, false) as NativeAdView
            binding.frAds.addView(adView)
            Admob.getInstance().populateNativeAdView(nativeLanguagePreload, adView)
        }
    }

    override fun onBackPressCustom() {
        finishAffinity()
    }

    override fun onClick(data: LanguageModelNo1) {
        EventTrackingHelper.logEvent(this, "language_fo_choose")
        if (!isChooseLanguage) {
            nativeManager?.reloadAdNow()
            if (countOpenSplash <= 10) {
                EventTrackingHelper.logEvent(this, "language_fo_choose" + "_${countOpenSplash}")
            }
        }
        isChooseLanguage = true
        model = data
        SystemUtilNo1.setLocale(this)
        binding.tvSelectLanguage.text = getLocalizedString(this, model.isoLanguage, R.string.please_select_language_to_continue)
        binding.tvTitle.text = getLocalizedString(this, model.isoLanguage, R.string.language)
        binding.ivDone.visible()
    }

    private fun setLanguageDefault(): List<LanguageModelNo1> {
        val lists: MutableList<LanguageModelNo1> = ArrayList()
        val systemLang: String = Resources.getSystem().configuration.locales[0].language
        Log.d("CHECK_systemLang", "systemLang: $systemLang ")

        lists.add(LanguageModelNo1("Español", "es", false, R.drawable.ic_span_flag))
        lists.add(LanguageModelNo1("Français", "fr", false, R.drawable.ic_french_flag))
        lists.add(LanguageModelNo1("हिन्दी", "hi", false, R.drawable.ic_hindi_flag))
        lists.add(LanguageModelNo1("English", "en", false, R.drawable.ic_english_flag))
        lists.add(LanguageModelNo1("Português (Brazil)", "pt-rBR", false, R.drawable.ic_brazil_flag))
        lists.add(LanguageModelNo1("Português (Portu)", "pt-rPT", false, R.drawable.ic_portuguese_flag))
        lists.add(LanguageModelNo1("日本語", "ja", false, R.drawable.ic_japan_flag))
        lists.add(LanguageModelNo1("Deutsch", "de", false, R.drawable.ic_german_flag))
        lists.add(LanguageModelNo1("中文 (简体)", "zh-rCN", false, R.drawable.ic_china_flag))
        lists.add(LanguageModelNo1("中文 (繁體) ", "zh-rTW", false, R.drawable.ic_china_flag))
        lists.add(LanguageModelNo1("عربي ", "ar", false, R.drawable.ic_a_rap_flag))
        lists.add(LanguageModelNo1("বাংলা ", "bn", false, R.drawable.ic_bengali_flag))
        lists.add(LanguageModelNo1("Русский ", "ru", false, R.drawable.ic_russia_flag))
        lists.add(LanguageModelNo1("Türkçe ", "tr", false, R.drawable.ic_turkey_flag))
        lists.add(LanguageModelNo1("한국인 ", "ko", false, R.drawable.ic_korean_flag))
        lists.add(LanguageModelNo1("Indonesia", "in", false, R.drawable.ic_indo_flag))

        val systemLangModel = lists.find { it.isoLanguage.contains(systemLang) }
        systemLangModel?.let {
            lists.remove(it)
            lists.add(3, it)
        }

        return lists
    }

    private fun getLocalizedString(context: Context, languageCode: String, resId: Int): String {

        val localeParts = languageCode.split("-")
        val myLocale = if (localeParts.size > 1) {
            Locale(localeParts[0], localeParts[1])
        } else {
            Locale(languageCode)
        }
        val config = Configuration(context.resources.configuration)
        config.setLocale(myLocale)
        val localizedContext = context.createConfigurationContext(config)
        return localizedContext.resources.getString(resId)
    }

    override fun initData() {
        countOpenSplash = RemoteConfigHelper.getInstance().get_config_long(this, "countOpenSplash")
        if (!RemoteConfigHelper.getInstance().get_config(this, "isLogEventLanguageFO")) {
            EventTrackingHelper.logEvent(this, Constants.TrackingKeys.language_fo_open)
            if (countOpenSplash <= 10) {
                Log.d(TAG, "logEventOnCreate: $countOpenSplash")
                EventTrackingHelper.logEvent(this, Constants.TrackingKeys.language_fo_open + "_${countOpenSplash}")
                RemoteConfigHelper.getInstance().set_config(this, "isLogEventLanguageFO", true)
            }
        }
        loadNativeLanguage()
        loadBannerAds(binding.frAdsBanner, Constants.RemoteKeys.banner_setting)
        preloadANativeMainIntro()
    }

    override fun initView() {
        adapter = LanguageStartAdapterNo1(
            setLanguageDefault(),
            this,
        )
        binding.rcvLang.adapter = adapter
        if (adapter?.isSelectLanguage() == true) {
            binding.ivDone.visible()
        } else {
            binding.ivDone.gone()
        }
        binding.ivDone.tap {
            if (Admob.getInstance().checkCondition(this, inter_splash) &&
                !TechManager.getInstance().isTech(this) &&
                Admob.getInstance().interstitialAdSplash != null
            ) {
                Admob.getInstance().showInterAds(this@LanguageStartNo1Activity, Admob.getInstance().interstitialAdSplash, object : InterCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        startNextAct()
                    }
                }, false, inter_splash)
            } else {
                startNextAct()
            }
        }
    }

    private fun startNextAct() {
        sharePref.isFirstSelectLanguage = false
        SystemUtilNo1.setPreLanguage(this@LanguageStartNo1Activity, model.isoLanguage)
        SystemUtilNo1.setLocale(this)
        SystemUtilNo1.changeLang(model.isoLanguage, this@LanguageStartNo1Activity)
        startActivity(Intent(this@LanguageStartNo1Activity, IntroNo1Activity::class.java))
        finishAffinity()
    }

    override fun getViewBinding(): ActivityLanguageStartNo1Binding {
        return ActivityLanguageStartNo1Binding.inflate(layoutInflater)
    }

    private fun loadNativeLanguage() {
        loadNative(
            binding.frAds,
            Constants.RemoteKeys.native_language,
            Constants.RemoteKeys.native_language,
            Constants.RemoteKeys.native_backup,
            R.layout.native_language_custom,
            R.layout.shimmer_native_lang_custom
        )
    }

    private fun preloadANativeMainIntro() {
        Admob.getInstance().loadNativeAds(
            this,
            AdmobApi.getInstance().getListIDByName(native_intro),
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                    super.onNativeAdLoaded(nativeAd)
                    nativeIntroPreload = nativeAd
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    isShowNativeIntroPreloadAtSplash = true
                }
            }, native_intro
        )
    }
}
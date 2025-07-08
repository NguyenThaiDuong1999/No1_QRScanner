package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.amazic.library.ads.banner_ads.BannerBuilder
import com.amazic.library.ads.banner_ads.BannerManager
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.callback.RewardedCallback
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerBuilder
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerManager
import com.amazic.library.ads.inter_ads.InterManager
import com.amazic.library.ads.native_ads.NativeBuilder
import com.amazic.library.ads.native_ads.NativeManager
import com.amazic.library.ads.reward_ads.RewardManager
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.CreateNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.splash.SplashNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.uninstall.ProblemNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.uninstall.UninstallNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.no_internet.NetworkReceiverNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.no_internet.NoInternetNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.SCREEN
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.SPLASH_ACTIVITY
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.SharePrefUtilsNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.toast

abstract class BaseNo1Activity<VB : ViewBinding> : AppCompatActivity() {

    abstract fun initView()
    abstract fun initData()
    abstract fun onBackPressCustom()
    lateinit var binding: VB
    private var currentApiVersion = 0
    private var networkReceiverNo1: NetworkReceiverNo1? = null
    val sharePref: SharePrefUtilsNo1 by lazy { SharePrefUtilsNo1(this) }
    var nativeManager: NativeManager? = null
    var collapseBannerManager: CollapseBannerManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtilNo1.setLocale(this)
        currentApiVersion = Build.VERSION.SDK_INT
        val flags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && this !is CreateNo1Activity) {
            window.decorView.systemUiVisibility = flags
            val decorView: View = window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        networkReceiverNo1 = NetworkReceiverNo1 {
            if (!it) {
                if (this !is SplashNo1Activity && this !is UninstallNo1Activity && this !is ProblemNo1Activity) {
                    showActivityCustom(NoInternetNo1Activity::class.java, null)
                }
            } else {
                if (this is NoInternetNo1Activity && intent?.extras?.getString(SCREEN) != SPLASH_ACTIVITY) {
                    finish()
                } else if (this is NoInternetNo1Activity && intent?.extras?.getString(SCREEN) == SPLASH_ACTIVITY) {
                    val myIntent = Intent(this, SplashNo1Activity::class.java)
                    myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(myIntent)
                }
            }
        }
        //registerReceiver(networkReceiverNo1, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressCustom()
            }
        }
        onBackPressedDispatcher.addCallback(this@BaseNo1Activity, onBackPressedCallback)
        initData()
        initView()
    }

    abstract fun getViewBinding(): VB

    override fun onResume() {
        super.onResume()
        if (Admob.getInstance().checkCondition(this@BaseNo1Activity, Constants.RemoteKeys.resume_wb) &&
            this@BaseNo1Activity !is SplashNo1Activity
        ) {
            AppOpenManager.getInstance().enableAppResumeWithActivity(javaClass)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(networkReceiverNo1)
    }

    fun showActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        intent.putExtras(bundle ?: Bundle())
        startActivity(intent)
    }

    fun showActivityCustom(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        intent.putExtras(bundle ?: Bundle())
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus && this !is CreateNo1Activity) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    fun Context.launchActivityIntent(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            toast("No valid app found")
        } catch (e: Exception) {
            toast(e.message.toString())
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { SystemUtilNo1.setLocale(it) })
    }

    fun loadCollapseBanner(adsKey: String) {
        val frContainerAds = findViewById<FrameLayout>(R.id.collapsible_banner_container_view)
        if (frContainerAds != null) {
            val collapseBannerBuilder = CollapseBannerBuilder()
            collapseBannerBuilder.setListId(AdmobApi.getInstance().getListIDByName(adsKey))
            collapseBannerManager = CollapseBannerManager(this, frContainerAds, this, collapseBannerBuilder, adsKey)
            collapseBannerManager?.setIntervalReloadBanner(
                RemoteConfigHelper.getInstance().get_config_long(this, Constants.RemoteKeys.collapse_banner_reload_interval) * 1000
            )
            collapseBannerManager?.setAlwaysReloadOnResume(true)
        }
    }

    fun loadBannerAds(
        frAds: FrameLayout,
        adsKey: String
    ) {
        val bannerBuilder = BannerBuilder(this, frAds, true)
        bannerBuilder.setListIdAdMain(AdmobApi.getInstance().getListIDByName(adsKey))
        val bannerManager = BannerManager(this, this, bannerBuilder, adsKey)
        //bannerManager.setAlwaysReloadOnResume(true)
        //if load multiple id native
        /*val bannerBuilder = BannerBuilder(this, frAds, true)
        bannerBuilder.setListIdAdMain(AdmobApi.getInstance().getListIDByName(adsKey))
        bannerBuilder.setListIdAdSecondary()
        bannerBuilder.setListIdAdBackup()
        val bannerManager = BannerManager(this, this, bannerBuilder, adsKey)
        bannerManager.remoteKeySecondary = ""
        bannerManager.remoteKeyBackup = ""
        bannerManager.setAlwaysReloadOnResume(true)*/
    }

    fun loadNative(
        frAds: FrameLayout,
        adsKeyMain: String,
        adsKeySecondary: String,
        adsKeyBackup: String,
        idLayoutNative: Int,
        idLayoutShimmer: Int,
        idNativeMeta: Int = idLayoutNative
    ) {
        val nativeBuilder = NativeBuilder(this, frAds, idLayoutShimmer, idLayoutNative, idNativeMeta, true)
        nativeBuilder.setListIdAdMain(AdmobApi.getInstance().getListIDByName(adsKeyMain))
        //set secondary list id ads if load double native
        nativeBuilder.setListIdAdSecondary(AdmobApi.getInstance().getListIDByName(adsKeySecondary))
        //set backup list id ads if need load backup ads when loading ads fail
        nativeBuilder.setListIdAdBackup(AdmobApi.getInstance().getListIDByName(adsKeyBackup))
        nativeManager = NativeManager(this, this, nativeBuilder, adsKeyMain)
        nativeManager?.remoteKeySecondary = adsKeySecondary
        nativeManager?.remoteKeyBackup = adsKeyBackup
        nativeManager?.timeOutCallAds = 12000
        nativeManager?.setIntervalReloadNative(RemoteConfigHelper.getInstance().get_config_long(this, Constants.RemoteKeys.interval_reload_native) * 1000)
        nativeManager?.setAlwaysReloadOnResume(true)
    }

    fun loadAndShowInter(
        adsKey: String,
        remoteKey: String,
        onNextAction: () -> Unit
    ) {
        InterManager.loadAndShowInterAds(this, adsKey, remoteKey, object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction.invoke()
            }
        })
    }

    fun loadInter(
        adsKey: String,
        remoteKey: String,
    ) {
        InterManager.loadInterAds(this, adsKey, remoteKey)
    }

    fun showInter(
        adsKey: String,
        remoteKey: String,
        isReloadAfterShow: Boolean,
        onNextAction: () -> Unit
    ) {
        InterManager.showInterAds(this, adsKey, remoteKey, object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction.invoke()
            }
        }, true, isReloadAfterShow)
    }

    fun loadReward(
        adsKey: String,
        remoteKey: String,
    ) {
        RewardManager.loadRewardAds(this, adsKey, remoteKey)
    }

    fun showReward(
        adsKey: String,
        remoteKey: String,
        isReloadAfterShow: Boolean,
        onNextAction: () -> Unit
    ) {
        var earnedReward = false
        RewardManager.showRewardAds(this, adsKey, remoteKey, object : RewardedCallback() {
            override fun onUserEarnedReward() {
                super.onUserEarnedReward()
                earnedReward = true
            }

            override fun onNextAction() {
                super.onNextAction()
                if (earnedReward) {
                    onNextAction.invoke()
                }
            }
        }, isReloadAfterShow)
    }

}
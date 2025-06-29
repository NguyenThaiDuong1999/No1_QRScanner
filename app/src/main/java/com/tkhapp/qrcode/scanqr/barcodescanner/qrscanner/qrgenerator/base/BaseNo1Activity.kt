package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.CreateNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.splash.SplashNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.uninstall.ProblemNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.uninstall.UninstallNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.no_internet.NetworkReceiverNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.no_internet.NoInternetNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.SCREEN
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.SPLASH_ACTIVITY
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
        registerReceiver(networkReceiverNo1, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiverNo1)
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
}
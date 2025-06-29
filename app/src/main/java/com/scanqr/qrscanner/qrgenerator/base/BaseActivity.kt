package com.scanqr.qrscanner.qrgenerator.base

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
import com.scanqr.qrscanner.qrgenerator.feature.main.create.CreateActivity
import com.scanqr.qrscanner.qrgenerator.feature.splash.SplashActivity
import com.scanqr.qrscanner.qrgenerator.feature.uninstall.ProblemActivity
import com.scanqr.qrscanner.qrgenerator.feature.uninstall.UninstallActivity
import com.scanqr.qrscanner.qrgenerator.no_internet.NetworkReceiver
import com.scanqr.qrscanner.qrgenerator.no_internet.NoInternetActivity
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.SCREEN
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.SPLASH_ACTIVITY
import com.scanqr.qrscanner.qrgenerator.utils.SharePrefUtils
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtil
import com.scanqr.qrscanner.qrgenerator.utils.toast

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    abstract fun initView()
    abstract fun initData()
    abstract fun onBackPressCustom()
    lateinit var binding: VB
    private var currentApiVersion = 0
    private var networkReceiver: NetworkReceiver? = null
    val sharePref: SharePrefUtils by lazy { SharePrefUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtil.setLocale(this)
        currentApiVersion = Build.VERSION.SDK_INT
        val flags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && this !is CreateActivity) {
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
        networkReceiver = NetworkReceiver {
            if (!it) {
                if (this !is SplashActivity && this !is UninstallActivity && this !is ProblemActivity) {
                    showActivityCustom(NoInternetActivity::class.java, null)
                }
            } else {
                if (this is NoInternetActivity && intent?.extras?.getString(SCREEN) != SPLASH_ACTIVITY) {
                    finish()
                } else if (this is NoInternetActivity && intent?.extras?.getString(SCREEN) == SPLASH_ACTIVITY) {
                    val myIntent = Intent(this, SplashActivity::class.java)
                    myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(myIntent)
                }
            }
        }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressCustom()
            }
        }
        onBackPressedDispatcher.addCallback(this@BaseActivity, onBackPressedCallback)
        initData()
        initView()
    }

    abstract fun getViewBinding(): VB

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
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
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus && this !is CreateActivity) {
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
        super.attachBaseContext(newBase?.let { SystemUtil.setLocale(it) })
    }
}
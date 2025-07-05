package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityPolicyNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.toast
import androidx.core.net.toUri

class PolicyNo1Activity : BaseNo1Activity<ActivityPolicyNo1Binding>() {

    override fun getViewBinding(): ActivityPolicyNo1Binding {
        return ActivityPolicyNo1Binding.inflate(layoutInflater)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        if (!isNetworkAvailable(this))
            toast("No internet!!!")
        binding.ivBack.tap {
            finish()
        }
        binding.viewPolicy.settings.javaScriptEnabled = true
        binding.viewPolicy.loadUrl("https://no1-webstore.onrender.com/No1_QRScanner.html")
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finish()
    }

}
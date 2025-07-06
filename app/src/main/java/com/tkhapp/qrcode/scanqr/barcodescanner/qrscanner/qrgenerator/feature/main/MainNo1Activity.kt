package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityMainNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.dialog.ExitDialogNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.history.HistoryNo1Fragment
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.home.HomeNo1Fragment
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.scan.ScanNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.setting.SettingNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper.SharePrefHelper
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_OPEN_URL
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_SOUND
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_VIBRATE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.checkCameraPermission
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.goToSettings
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.showRationaleDialog
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap

class MainNo1Activity : BaseNo1Activity<ActivityMainNo1Binding>() {
    override fun initView() {
        loadCollapseBanner(Constants.RemoteKeys.collapse_home)
        showViewHomeClick()
        if (!SharePrefHelper(this).contain(IS_VIBRATE)) {
            SharePrefHelper(this).saveBoolean(IS_VIBRATE, true)
        }
        if (!SharePrefHelper(this).contain(IS_SOUND)) {
            SharePrefHelper(this).saveBoolean(IS_SOUND, true)
        }
        if (!SharePrefHelper(this).contain(IS_OPEN_URL)) {
            SharePrefHelper(this).saveBoolean(IS_OPEN_URL, false)
        }
        binding.llHome.tap {
            collapseBannerManager?.reloadAdNow()
            showViewHomeClick()
        }
        binding.llHistory.tap {
            collapseBannerManager?.reloadAdNow()
            showViewHistoryClick()
        }
        binding.ivSetting.tap {
            startActivity(Intent(this@MainNo1Activity, SettingNo1Activity::class.java))
        }
        binding.icScan.tap {
            checkCameraPermission(
                onPermissionGranted = {
                    startActivity(Intent(this@MainNo1Activity, ScanNo1Activity::class.java))
                    finish()
                },
                onShowRationale = {
                    showRationaleDialog(getString(R.string.this_app_need_camera_permission_to_enhance_feature)) {

                        goToSettings()
                    }
                },
                onRequestPermission = {

                }
            )
        }
    }

    override fun initData() {

    }

    private fun showFragment(fragment: Fragment, bundle: Bundle?) {
        fragment.arguments = bundle
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.fr_container, fragment)
        trans.commit()
    }

    private fun showViewHomeClick() {
        setSelectedBottomItemHome()
        showFragment(HomeNo1Fragment(), null)
    }

    private fun showViewHistoryClick() {
        setSelectedBottomItemHistory()
        showFragment(HistoryNo1Fragment(), null)
    }

    override fun getViewBinding(): ActivityMainNo1Binding {
        return ActivityMainNo1Binding.inflate(layoutInflater)
    }

    private fun setSelectedBottomItemHome() {
        binding.ivHome.setImageResource(R.drawable.ic_home_selected)
        binding.tvHome.setTextColor(Color.parseColor("#2962FF"))
        binding.icBottomHomeSelected.gone()

        binding.ivHistory.setImageResource(R.drawable.ic_history)
        binding.tvHistory.setTextColor(Color.parseColor("#8E8E93"))
        binding.icBottomHistorySelected.gone()
    }

    private fun setSelectedBottomItemHistory() {
        binding.ivHistory.setImageResource(R.drawable.ic_history_home_selected)
        binding.tvHistory.setTextColor(Color.parseColor("#2962FF"))
        binding.icBottomHistorySelected.gone()

        binding.ivHome.setImageResource(R.drawable.ic_home_unselect)
        binding.tvHome.setTextColor(Color.parseColor("#8E8E93"))
        binding.icBottomHomeSelected.gone()
    }

    override fun onBackPressCustom() {
        if (supportFragmentManager.findFragmentById(R.id.fr_container) is HomeNo1Fragment) {
            val exitDialogNo1 = ExitDialogNo1(
                onNegative = {

                },
                onPositive = {
                    finishAffinity()
                }
            )
            exitDialogNo1.show(supportFragmentManager, exitDialogNo1.tag)
        }
    }
}
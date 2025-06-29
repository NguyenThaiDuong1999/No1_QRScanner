package com.scanqr.qrscanner.qrgenerator.feature.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityMainBinding
import com.scanqr.qrscanner.qrgenerator.dialog.ExitDialog
import com.scanqr.qrscanner.qrgenerator.feature.main.history.HistoryFragment
import com.scanqr.qrscanner.qrgenerator.feature.main.home.HomeFragment
import com.scanqr.qrscanner.qrgenerator.feature.scan.ScanActivity
import com.scanqr.qrscanner.qrgenerator.feature.setting.SettingActivity
import com.scanqr.qrscanner.qrgenerator.helper.SharePrefHelper
import com.scanqr.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_OPEN_URL
import com.scanqr.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_SOUND
import com.scanqr.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_VIBRATE
import com.scanqr.qrscanner.qrgenerator.utils.checkCameraPermission
import com.scanqr.qrscanner.qrgenerator.utils.goToSettings
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.showRationaleDialog
import com.scanqr.qrscanner.qrgenerator.utils.tap

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initView() {
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
            showViewHomeClick()
        }
        binding.llHistory.tap {
            showViewHistoryClick()
        }
        binding.ivSetting.tap {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
        }
        binding.icScan.tap {
            checkCameraPermission(
                onPermissionGranted = {
                    startActivity(Intent(this@MainActivity, ScanActivity::class.java))
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
        binding.title.text = getString(R.string.title_create)
        binding.desc.text = getString(R.string.desc_create)
        showFragment(HomeFragment(), null)
    }

    private fun showViewHistoryClick() {
        setSelectedBottomItemHistory()
        binding.title.text = getString(R.string.title_history)
        binding.desc.text = getString(R.string.desc_history)
        showFragment(HistoryFragment(), null)
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setSelectedBottomItemHome() {
        binding.ivHome.setImageResource(R.drawable.ic_home_selected)
        binding.tvHome.setTextColor(Color.parseColor("#1F1F29"))
        binding.icBottomHomeSelected.gone()

        binding.ivHistory.setImageResource(R.drawable.ic_history)
        binding.tvHistory.setTextColor(Color.parseColor("#8E8E93"))
        binding.icBottomHistorySelected.gone()
    }

    private fun setSelectedBottomItemHistory() {
        binding.ivHistory.setImageResource(R.drawable.ic_history_home_selected)
        binding.tvHistory.setTextColor(Color.parseColor("#1F1F29"))
        binding.icBottomHistorySelected.gone()

        binding.ivHome.setImageResource(R.drawable.ic_home_unselect)
        binding.tvHome.setTextColor(Color.parseColor("#8E8E93"))
        binding.icBottomHomeSelected.gone()
    }

    override fun onBackPressCustom() {
        if (supportFragmentManager.findFragmentById(R.id.fr_container) is HomeFragment) {
            val exitDialog = ExitDialog(
                onNegative = {

                },
                onPositive = {
                    finishAffinity()
                }
            )
            exitDialog.show(supportFragmentManager, exitDialog.tag)
        }
    }
}
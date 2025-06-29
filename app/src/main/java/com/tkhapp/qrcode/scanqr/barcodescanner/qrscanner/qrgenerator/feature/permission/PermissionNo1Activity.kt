package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityPermissionNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.dialog.ConfirmDialogNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper.SharePrefHelper
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.SharePrefKey.IS_SETTING_CONTINUE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.goToSettings
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isLargerTiramisu
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap


class PermissionNo1Activity : BaseNo1Activity<ActivityPermissionNo1Binding>() {

    override fun initView() {
        binding.switchCamera.tap {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                val confirmDialogNo1 = ConfirmDialogNo1(
                    title = getString(R.string.permission_required),
                    desc = getString(R.string.this_app_need_camera_permission_to_enhance_feature),
                    negativeMsg = getString(R.string.cancel),
                    positiveMsg = getString(R.string.Go_to_setting),
                    bgNegative = R.drawable.bg_grey,
                    bgPositive = R.drawable.bg_next_action_scan,
                    onNegative = {

                    },
                    onPositive = {

                        goToSettings()
                    }
                )
                confirmDialogNo1.show(supportFragmentManager, confirmDialogNo1.tag)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 1111)
            }
        }
        binding.switchStorage.tap {
            if (isLargerTiramisu()) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                    val confirmDialogNo1 = ConfirmDialogNo1(
                        title = getString(R.string.permission_required),
                        desc = getString(R.string.this_app_need_read_images_permission_to_enhance_feature),
                        negativeMsg = getString(R.string.cancel),
                        positiveMsg = getString(R.string.Go_to_setting),
                        bgNegative = R.drawable.bg_grey,
                        bgPositive = R.drawable.bg_next_action_scan,
                        onNegative = {

                        },
                        onPositive = {

                            goToSettings()
                        }
                    )
                    confirmDialogNo1.show(supportFragmentManager, confirmDialogNo1.tag)
                } else {
                    requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 2222)
                }
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ) {
                    val confirmDialogNo1 = ConfirmDialogNo1(
                        title = getString(R.string.permission_required),
                        desc = getString(R.string.this_app_need_external_storage_to_enhance_feature),
                        negativeMsg = getString(R.string.cancel),
                        positiveMsg = getString(R.string.Go_to_setting),
                        bgNegative = R.drawable.bg_grey,
                        bgPositive = R.drawable.bg_next_action_scan,
                        onNegative = {

                        },
                        onPositive = {

                            goToSettings()
                        }
                    )
                    confirmDialogNo1.show(supportFragmentManager, confirmDialogNo1.tag)
                } else {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 2222)
                }
            }
        }

        binding.tvContinue.tap {
            SharePrefHelper(this).saveBoolean(IS_SETTING_CONTINUE, true)
            startActivity(Intent(this, MainNo1Activity::class.java))
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        if (checkPermission(arrayOf(Manifest.permission.CAMERA))) {
            binding.switchCamera.isClickable = false
            binding.switchCamera.setImageResource(R.drawable.switch_true)
        } else {
            binding.switchCamera.setImageResource(R.drawable.switch_false)
        }
        if (isLargerTiramisu() && checkPermission(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))) {
            binding.switchStorage.isClickable = false
            binding.switchStorage.setImageResource(R.drawable.switch_true)
        } else if (!isLargerTiramisu() && checkPermission(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        ) {
            binding.switchStorage.isClickable = false
            binding.switchStorage.setImageResource(R.drawable.switch_true)
        } else if (isLargerTiramisu() && !checkPermission(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))) {
            binding.switchStorage.setImageResource(R.drawable.switch_false)
        } else if (!isLargerTiramisu() && !checkPermission(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        ) {
            binding.switchStorage.setImageResource(R.drawable.switch_false)
        }
    }

    private fun checkPermission(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            val allowPer = ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            if (!allowPer) return false
        }
        return true
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finishAffinity()
    }

    override fun getViewBinding(): ActivityPermissionNo1Binding {
        return ActivityPermissionNo1Binding.inflate(layoutInflater)
    }
}
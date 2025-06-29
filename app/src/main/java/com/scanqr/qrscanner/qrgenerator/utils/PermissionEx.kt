package com.scanqr.qrscanner.qrgenerator.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.dialog.ConfirmDialog

fun Context.checkPermission(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        val allowPer = ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
        if (!allowPer) return false
    }
    return true
}

fun Activity.checkExternalStoragePermission(onPermissionGranted: () -> Unit, onShowRationale: () -> Unit, onRequestPermission: () -> Unit) {
    if (isLargerTiramisu()) {
        if (checkPermission(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))) {
            onPermissionGranted.invoke()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                onShowRationale.invoke()
            } else {
                onRequestPermission.invoke()
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 1111)
            }
        }
    } else {
        if (checkPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            onPermissionGranted.invoke()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                onShowRationale.invoke()
            } else {
                onRequestPermission.invoke()
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1111)
            }
        }
    }
}

fun Activity.checkCameraPermission(onPermissionGranted: () -> Unit, onShowRationale: () -> Unit, onRequestPermission: () -> Unit) {
    if (checkPermission(arrayOf(Manifest.permission.CAMERA))) {
        onPermissionGranted.invoke()
    } else {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            onShowRationale.invoke()
        } else {
            onRequestPermission.invoke()
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 2222)
        }
    }
}

fun Context.isLargerTiramisu(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}

fun FragmentActivity.showRationaleDialog(desc: String, onGoToSetting: () -> Unit) {
    val confirmDialog = ConfirmDialog(
        title = getString(R.string.permission_required),
        desc = desc,
        negativeMsg = getString(R.string.cancel),
        positiveMsg = getString(R.string.Go_to_setting),
        bgNegative = R.drawable.bg_grey,
        bgPositive = R.drawable.bg_next_action_scan,
        onNegative = {

        },
        onPositive = {
            onGoToSetting.invoke()
        }
    )
    confirmDialog.show(supportFragmentManager, confirmDialog.tag)
}

fun Fragment.goToSettings() {
    val intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri =
        Uri.fromParts("package", requireActivity().packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Activity.goToSettings() {
    val intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri =
        Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    startActivity(intent)
}
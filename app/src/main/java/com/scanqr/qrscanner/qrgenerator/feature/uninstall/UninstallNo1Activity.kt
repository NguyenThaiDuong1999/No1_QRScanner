package com.scanqr.qrscanner.qrgenerator.feature.uninstall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Activity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityUninstallNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.scanqr.qrscanner.qrgenerator.no_internet.NoInternetNo1Activity
import com.scanqr.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.SCREEN
import com.scanqr.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.SPLASH_ACTIVITY
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.launchActivity
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class UninstallNo1Activity : BaseNo1Activity<ActivityUninstallNo1Binding>() {
    private var uninstallAdapterNo1: UninstallAdapterNo1? = null

    override fun initView() {
        val listData = mutableListOf<AnswerModelNo1>().apply {
            add(AnswerModelNo1(name = R.string.difficult_to_use, isSelected = true))
            add(AnswerModelNo1(name = R.string.too_many_ads))
            add(AnswerModelNo1(name = R.string.scan_history_not_saved))
            add(AnswerModelNo1(name = R.string.some_qr_codes_cannot_be_scanned))
            add(AnswerModelNo1(name = R.string.others))
        }
        binding.apply {
            uninstallAdapterNo1 = UninstallAdapterNo1(
                onClick = { data, pos ->
                    uninstallAdapterNo1?.selectAnswer(data)
                    if (pos == listData.size - 1) {
                        rlOthersContent.visible()
                    } else {
                        rlOthersContent.gone()
                    }
                }, listData
            )

            rvAnswer.adapter = uninstallAdapterNo1

            tvUninstall.tap {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
            tvCancel.tap {
                if (SystemUtilNo1.haveNetworkConnection(this@UninstallNo1Activity)) {
                    launchActivity(MainNo1Activity::class.java)
                    finishAffinity()
                } else {
                    launchActivity(Bundle().apply {
                        putString(SCREEN, SPLASH_ACTIVITY)
                    }, NoInternetNo1Activity::class.java)
                }
            }
            ivBack.tap {
                finish()
            }
            /*edAnswer.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.length > 30) {
                        toast(getString(R.string.maximum_30_characters))
                    }
                }
            })*/
            edAnswer.movementMethod = ScrollingMovementMethod()
        }
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finish()
    }

    override fun getViewBinding(): ActivityUninstallNo1Binding = ActivityUninstallNo1Binding.inflate(layoutInflater)

}
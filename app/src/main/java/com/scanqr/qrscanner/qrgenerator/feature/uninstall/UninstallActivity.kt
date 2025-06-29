package com.scanqr.qrscanner.qrgenerator.feature.uninstall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityUninstallBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainActivity
import com.scanqr.qrscanner.qrgenerator.no_internet.NoInternetActivity
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.SCREEN
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.SPLASH_ACTIVITY
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtil
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.launchActivity
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class UninstallActivity : BaseActivity<ActivityUninstallBinding>() {
    private var uninstallAdapter: UninstallAdapter? = null

    override fun initView() {
        val listData = mutableListOf<AnswerModel>().apply {
            add(AnswerModel(name = R.string.difficult_to_use, isSelected = true))
            add(AnswerModel(name = R.string.too_many_ads))
            add(AnswerModel(name = R.string.scan_history_not_saved))
            add(AnswerModel(name = R.string.some_qr_codes_cannot_be_scanned))
            add(AnswerModel(name = R.string.others))
        }
        binding.apply {
            uninstallAdapter = UninstallAdapter(
                onClick = { data, pos ->
                    uninstallAdapter?.selectAnswer(data)
                    if (pos == listData.size - 1) {
                        rlOthersContent.visible()
                    } else {
                        rlOthersContent.gone()
                    }
                }, listData
            )

            rvAnswer.adapter = uninstallAdapter

            tvUninstall.tap {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
            tvCancel.tap {
                if (SystemUtil.haveNetworkConnection(this@UninstallActivity)) {
                    launchActivity(MainActivity::class.java)
                    finishAffinity()
                } else {
                    launchActivity(Bundle().apply {
                        putString(SCREEN, SPLASH_ACTIVITY)
                    }, NoInternetActivity::class.java)
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

    override fun getViewBinding(): ActivityUninstallBinding = ActivityUninstallBinding.inflate(layoutInflater)

}
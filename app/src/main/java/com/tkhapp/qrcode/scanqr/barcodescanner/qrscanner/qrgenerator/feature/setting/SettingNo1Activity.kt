package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.setting

import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.BuildConfig
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivitySettingNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language.LanguageNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper.SharePrefHelper
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.RatingDialogNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.SharePrefUtilsNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.SharePrefKey.IS_OPEN_URL
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.SharePrefKey.IS_SOUND
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.SharePrefKey.IS_VIBRATE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.logI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class SettingNo1Activity : BaseNo1Activity<ActivitySettingNo1Binding>() {

    private var isVibrate = false
    private var isSound = false
    private var isOpenUrl = false

    override fun initView() {
        binding.icBack.tap {
            finish()
        }
    }

    override fun initData() {

        isVibrate = SharePrefHelper(this).getBoolean(IS_VIBRATE)
        isSound = SharePrefHelper(this).getBoolean(IS_SOUND)
        isOpenUrl = SharePrefHelper(this).getBoolean(IS_OPEN_URL)

        if (SharePrefHelper(this).getBoolean(IS_VIBRATE)) {
            binding.layoutVibrate.switchSetting.setImageResource(R.drawable.switch_true)
        } else {
            binding.layoutVibrate.switchSetting.setImageResource(R.drawable.switch_false)
        }
        if (SharePrefHelper(this).getBoolean(IS_SOUND)) {
            binding.layoutSound.switchSetting.setImageResource(R.drawable.switch_true)
        } else {
            binding.layoutSound.switchSetting.setImageResource(R.drawable.switch_false)
        }
        if (SharePrefHelper(this).getBoolean(IS_OPEN_URL)) {
            binding.layoutOpenUrl.switchSetting.setImageResource(R.drawable.switch_true)
        } else {
            binding.layoutOpenUrl.switchSetting.setImageResource(R.drawable.switch_false)
        }

        binding.layoutLang.apply {
            showSettingItem(
                "lang",
                ivSetting,
                tvSetting,
                R.drawable.ic_setting_lang,
                getString(R.string.language),
                layoutLang,
                switchSetting
            )
            tvCurrentLang.text = SystemUtilNo1.listLanguage()
                .find { it.isoLanguage == SystemUtilNo1.getPreLanguage(this@SettingNo1Activity) }?.languageName
        }
        binding.layoutVibrate.apply {
            showSettingItem(
                "switch",
                ivSetting,
                tvSetting,
                R.drawable.ic_setting_vibrate,
                getString(R.string.vibrate),
                layoutLang,
                switchSetting
            )
        }
        binding.layoutSound.apply {
            showSettingItem(
                "switch",
                ivSetting,
                tvSetting,
                R.drawable.ic_setting_sound,
                getString(R.string.sound),
                layoutLang,
                switchSetting
            )
        }
        binding.layoutOpenUrl.apply {
            showSettingItem(
                "switch",
                ivSetting,
                tvSetting,
                R.drawable.ic_setting_open_url,
                getString(R.string.open_url_setting),
                layoutLang,
                switchSetting
            )
        }
        binding.layoutShare.apply {
            showSettingItem(
                "none",
                ivSetting,
                tvSetting,
                R.drawable.ic_setting_share,
                getString(R.string.share),
                layoutLang,
                switchSetting
            )
        }
        binding.layoutRate.apply {
            showSettingItem(
                "none",
                ivSetting,
                tvSetting,
                R.drawable.ic_setting_rate,
                getString(R.string.rate),
                layoutLang,
                switchSetting
            )
        }
        binding.layoutPrivacy.apply {
            showSettingItem(
                "none",
                ivSetting,
                tvSetting,
                R.drawable.ic_setting_privacy,
                getString(R.string.privacy_policy),
                layoutLang,
                switchSetting
            )
        }
        binding.llLanguage.tap {
            startActivity(Intent(this@SettingNo1Activity, LanguageNo1Activity::class.java))
        }
        binding.layoutVibrate.switchSetting.tap {
            isVibrate = !isVibrate
            if (isVibrate) {
                binding.layoutVibrate.switchSetting.setImageResource(R.drawable.switch_true)
                SharePrefHelper(this).saveBoolean(IS_VIBRATE, true)
            } else {
                binding.layoutVibrate.switchSetting.setImageResource(R.drawable.switch_false)
                SharePrefHelper(this).saveBoolean(IS_VIBRATE, false)
            }
        }
        binding.layoutSound.switchSetting.tap {
            isSound = !isSound
            if (isSound) {
                binding.layoutSound.switchSetting.setImageResource(R.drawable.switch_true)
                SharePrefHelper(this).saveBoolean(IS_SOUND, true)
            } else {
                binding.layoutSound.switchSetting.setImageResource(R.drawable.switch_false)
                SharePrefHelper(this).saveBoolean(IS_SOUND, false)
            }
        }
        binding.layoutOpenUrl.switchSetting.tap {
            isOpenUrl = !isOpenUrl
            if (isOpenUrl) {
                binding.layoutOpenUrl.switchSetting.setImageResource(R.drawable.switch_true)
                SharePrefHelper(this).saveBoolean(IS_OPEN_URL, true)
            } else {
                binding.layoutOpenUrl.switchSetting.setImageResource(R.drawable.switch_false)
                SharePrefHelper(this).saveBoolean(IS_OPEN_URL, false)
            }
        }
        binding.llShare.tap {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                val msg =
                    "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
                intent.putExtra(Intent.EXTRA_TEXT, msg)
                startActivity(Intent.createChooser(intent, "choose one"))
            } catch (e: Exception) {

            }
        }
        binding.llPolicy.tap {
            startActivity(Intent(this@SettingNo1Activity, PolicyNo1Activity::class.java))
        }
        binding.llRate.tap {
            val dialog =
                com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.RatingDialogNo1(
                    this
                )
            dialog.init(this, object : com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.RatingDialogNo1.OnPress {
                override fun send(rate: Float) {
                    Toast.makeText(
                        this@SettingNo1Activity,
                        getString(R.string.thanks_for_your_rate),
                        Toast.LENGTH_SHORT
                    ).show()
                    com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.SharePrefUtilsNo1.forceRated(this@SettingNo1Activity)
                    Handler(mainLooper).postDelayed({
                        if (com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.SharePrefUtilsNo1.isRated(this@SettingNo1Activity)) {
                            binding.llRate.gone()
                        } else {
                            binding.llRate.visible()
                        }
                    }, 250)
                    dialog.dismiss()
                }

                override fun rating(rate: Float) {
                    Log.i("rating", "on")
                    this@SettingNo1Activity.let { activity ->
                        val manager: ReviewManager = ReviewManagerFactory.create(activity)
                        val request: Task<ReviewInfo> = manager.requestReviewFlow()
                        request.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.i("rating", "success")
                                val reviewInfo: ReviewInfo = task.result
                                val flow: Task<Void> =
                                    manager.launchReviewFlow(activity, reviewInfo)
                                flow.addOnSuccessListener {
                                    com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.SharePrefUtilsNo1.forceRated(activity)
                                    dialog.dismiss()
                                    Handler(mainLooper).postDelayed({
                                        if (com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.SharePrefUtilsNo1.isRated(this@SettingNo1Activity)) {
                                            binding.llRate.gone()
                                        } else {
                                            binding.llRate.visible()
                                        }
                                    }, 250)
                                }
                                dialog.dismiss()
                            } else {
                                dialog.dismiss()
                            }
                        }
                    }
                    dialog.dismiss()
                }

                override fun cancel() {
                    dialog.dismiss()
                }

                override fun later() {
                    dialog.dismiss()
                }
            })
            dialog.show()
        }
    }

    override fun onBackPressCustom() {
        finish()
    }

    private fun showSettingItem(
        type: String,
        icon: ImageView,
        tvName: TextView,
        imgIcon: Int,
        name: String,
        lnLang: LinearLayout,
        switch: ImageView
    ) {
        logI("show", type)
        logI("show", name)
        icon.setImageResource(imgIcon)
        tvName.text = name
        when (type) {
            "lang" -> {
                lnLang.visible()
                switch.gone()
            }

            "switch" -> {
                lnLang.gone()
                switch.visible()
            }

            "none" -> {
                lnLang.gone()
                switch.gone()
            }
        }
    }

    override fun getViewBinding(): ActivitySettingNo1Binding {
        return ActivitySettingNo1Binding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        if (com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.rate.SharePrefUtilsNo1.isRated(this)) {
            binding.llRate.gone()
        } else {
            binding.llRate.visible()
        }
    }

}
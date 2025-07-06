package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import androidx.core.content.ContextCompat
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityCreateNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutCreateContactNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutCreateEmailNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutCreateLocationNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutCreateSmsNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutCreateWifiNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.include.IncludeCreateContactHandlerNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.include.IncludeCreateEmailHandlerNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.include.IncludeCreateLocationHandlerNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.include.IncludeCreateMessageHandlerNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.include.IncludeCreateWifiHandlerNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.Type
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.TypeUI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.result_scan.ScanResultNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.KEY_SCREEN_INTO_CREATE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.KEY_SCREEN_TO_RESULT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.checkOnEditTextChange
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.getCurrentDate
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.getCurrentHHMM
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isFacebookUrl
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isInstagramUrl
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isTwitterUrl
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isWhatsappUrl
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isYouTubeUrl
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.setOnFocus
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.showError
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class CreateNo1Activity : BaseNo1Activity<ActivityCreateNo1Binding>() {

    private var listShort: MutableList<String> = mutableListOf()
    private var listLong: MutableList<String> = mutableListOf()
    private var listMultiple: MutableList<String> = mutableListOf()
    private var screenKey = ""

    private fun loadNativeCreate() {
        loadNative(
            binding.frAds,
            Constants.RemoteKeys.native_create,
            Constants.RemoteKeys.native_create,
            Constants.RemoteKeys.native_backup,
            R.layout.native_large_ads_with_button_above,
            R.layout.shimmer_native_large_with_button_above
        )
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        loadNativeCreate()

        binding.icBack.tap {
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
            window.insetsController?.show(WindowInsets.Type.navigationBars() or WindowInsets.Type.statusBars())
            window.navigationBarColor = getColor(R.color.black)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.navigationBarColor = getColor(R.color.black)
        }

        when {
            listShort.contains(screenKey) -> {
                binding.layoutShort.visible()
                binding.layoutLong.gone()
                binding.layoutMultiple.gone()
            }

            listLong.contains(screenKey) -> {
                binding.layoutLong.visible()
                binding.layoutShort.gone()
                binding.layoutMultiple.gone()
                binding.layoutCreateLong.edtContent.checkOnEditTextChange { currentChar ->
                    runOnUiThread {
                        binding.layoutCreateLong.tvCountChar.text = "$currentChar/1000"
                        binding.layoutCreateLong.cvContent.strokeColor =
                            ContextCompat.getColor(this, R.color.white)
                        binding.layoutCreateLong.tvEmpty.gone()
                    }
                }
            }
        }
        when (screenKey) {
            Type.TEXT.toString() -> {
                binding.layoutCreateLong.edtContent.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                binding.tvTypeCreate.text = getString(R.string.text)
                binding.layoutCreateLong.tvCountChar.text = "0/1000"
                binding.layoutCreateLong.edtContent.filters = arrayOf(InputFilter.LengthFilter(1000))
                binding.layoutCreateLong.edtContent.checkOnEditTextChange {
                    runOnUiThread {
                        binding.layoutCreateLong.tvCountChar.text = "$it/1000"
                    }
                    setOnFocus(binding.layoutCreateLong.tvEmpty, binding.layoutCreateLong.cvContent)
                }
                binding.layoutCreate.tap {
                    loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                        onNextAction = {
                            binding.layoutCreateLong.apply {
                                if (edtContent.text.toString().trim().isEmpty()) {
                                    showError(tvEmpty, cvContent, getString(R.string.please_enter_content))
                                } else {
                                    val bundle = Bundle().apply {
                                        putString(
                                            Constants.ScreenKey.CONTENT_TEXT,
                                            edtContent.text.toString()
                                        )
                                    }
                                    goToResultActivity(bundle, Type.TEXT.toString(), TypeUI.SINGLE)
                                }
                            }
                        }
                    )
                }
            }

            Type.URL.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.website)
                binding.layoutCreateLong.tvType.text = getString(R.string.website)
                binding.layoutCreateLong.edtContent.filters = arrayOf(InputFilter.LengthFilter(1000))
                binding.layoutCreateLong.edtContent.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                binding.tvTypeCreate.text = getString(R.string.website)
                binding.layoutCreateLong.edtContent.hint = "http://"
                binding.layoutCreateLong.edtContent.checkOnEditTextChange {
                    runOnUiThread {
                        binding.layoutCreateLong.tvCountChar.text = "$it/1000"
                    }
                    setOnFocus(binding.layoutCreateLong.tvEmpty, binding.layoutCreateLong.cvContent)
                }
                binding.layoutCreate.tap {
                    loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                        onNextAction = {
                            binding.layoutCreateLong.apply {
                                if (edtContent.text.toString().trim().isEmpty()) {
                                    showError(tvEmpty, cvContent, getString(R.string.please_enter_content))
                                } else if (!Patterns.WEB_URL.matcher(edtContent.text.toString().trim())
                                        .matches()
                                ) {
                                    showError(tvEmpty, cvContent, getString(R.string.invalid_url))
                                } else {
                                    val bundle = Bundle().apply {
                                        putString(
                                            Constants.ScreenKey.CONTENT_URL,
                                            binding.layoutCreateLong.edtContent.text.toString()
                                        )
                                    }
                                    goToResultActivity(bundle, Type.URL.toString(), TypeUI.SINGLE)
                                }
                            }
                        }
                    )
                }
            }

            Type.WIFI.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.wifi_create)
                binding.llCreateWifi.visible()
                val wifiHandler = IncludeCreateWifiHandlerNo1(
                    this,
                    LayoutCreateWifiNo1Binding.bind(binding.layoutCreateWifi.root)
                )
                wifiHandler.apply {
                    initView()
                    binding.layoutCreate.tap {
                        loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                            onNextAction = {
                                onCreateClick { content ->
                                    val bundle = Bundle().apply {
                                        putString(Constants.ScreenKey.CONTENT_WIFI, content)
                                    }
                                    goToResultActivity(bundle, Type.WIFI.toString(), TypeUI.MULTIPLE)
                                }
                            }
                        )
                    }
                }
            }

            Type.EMAIL.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.email)
                binding.llCreateEmail.visible()
                val emailHandler = IncludeCreateEmailHandlerNo1(
                    this,
                    LayoutCreateEmailNo1Binding.bind(binding.layoutCreateEmail.root)
                )
                emailHandler.apply {
                    initView()
                    binding.layoutCreate.tap {
                        loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                            onNextAction = {
                                onCreateClick { content ->
                                    val bundle = Bundle().apply {
                                        putString(Constants.ScreenKey.CONTENT_EMAIL, content)
                                    }
                                    goToResultActivity(bundle, Type.EMAIL.toString(), TypeUI.MULTIPLE)
                                }
                            }
                        )
                    }
                }
            }

            Type.LOCATION.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.location)
                binding.llCreateLocation.visible()
                val locationHandler = IncludeCreateLocationHandlerNo1(
                    this,
                    LayoutCreateLocationNo1Binding.bind(binding.layoutCreateLocation.root)
                )
                locationHandler.apply {
                    initView()
                    binding.layoutCreate.tap {
                        loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                            onNextAction = {
                                onCreateClick { content ->
                                    val bundle = Bundle().apply {
                                        putString(Constants.ScreenKey.CONTENT_LOCATION, content)
                                    }
                                    goToResultActivity(bundle, Type.LOCATION.toString(), TypeUI.MULTIPLE)
                                }
                            }
                        )
                    }
                }
            }

            Type.CONTACT.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.contact)
                binding.llCreateContact.visible()
                val contactHandler = IncludeCreateContactHandlerNo1(
                    this,
                    LayoutCreateContactNo1Binding.bind(binding.layoutCreateContact.root)
                )
                contactHandler.apply {
                    initView()
                    binding.layoutCreate.tap {
                        loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                            onNextAction = {
                                onCreateClick { content ->
                                    val bundle = Bundle().apply {
                                        putString(Constants.ScreenKey.CONTENT_CONTACT, content)
                                    }
                                    goToResultActivity(bundle, Type.CONTACT.toString(), TypeUI.MULTIPLE)
                                }
                            }
                        )
                    }
                }
            }

            Type.PHONE.toString() -> {
                binding.layoutCreateShort.tvType.text = getString(R.string.number_phone)
                binding.tvTypeCreate.text = getString(R.string.number_phone)
                binding.layoutCreateShort.edtContent.inputType = InputType.TYPE_CLASS_NUMBER
                binding.layoutCreateShort.tvCountChar.text = "0/20"
                binding.layoutCreateShort.edtContent.filters = arrayOf(InputFilter.LengthFilter(20))
                binding.layoutCreateShort.edtContent.checkOnEditTextChange {
                    runOnUiThread {
                        binding.layoutCreateShort.tvCountChar.text = "$it/20"
                    }
                    setOnFocus(
                        binding.layoutCreateShort.tvEmpty,
                        binding.layoutCreateShort.cvContent
                    )
                }
                binding.layoutCreate.tap {
                    loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                        onNextAction = {
                            if (binding.layoutCreateShort.edtContent.text.toString().trim().isEmpty()) {
                                showError(
                                    binding.layoutCreateShort.tvEmpty,
                                    binding.layoutCreateShort.cvContent,
                                    getString(R.string.please_enter_content)
                                )
                            } else {
                                val bundle = Bundle().apply {
                                    putString(
                                        Constants.ScreenKey.CONTENT_PHONE,
                                        "tel:${binding.layoutCreateShort.edtContent.text.toString().trim()}"
                                    )
                                }
                                goToResultActivity(bundle, Type.PHONE.toString(), TypeUI.SINGLE)
                            }
                        }
                    )
                }
            }

            Type.SMS.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.sms)
                binding.llCreateSms.visible()
                val smsHandler = IncludeCreateMessageHandlerNo1(
                    this,
                    LayoutCreateSmsNo1Binding.bind(binding.layoutCreateSms.root)
                )
                smsHandler.apply {
                    initView()
                    binding.layoutCreate.tap {
                        loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                            onNextAction = {
                                onCreateClick { content ->
                                    val bundle = Bundle().apply {
                                        putString(Constants.ScreenKey.CONTENT_SMS, content)
                                    }
                                    goToResultActivity(bundle, Type.SMS.toString(), TypeUI.MULTIPLE)
                                }
                            }
                        )
                    }
                }
            }

            Type.BARCODE.toString() -> {
                binding.layoutCreateShort.tvType.text = getString(R.string.bar_code)
                binding.tvTypeCreate.text = getString(R.string.bar_code)
                binding.layoutCreateShort.edtContent.inputType = InputType.TYPE_CLASS_NUMBER
                binding.layoutCreateShort.edtContent.filters = arrayOf(InputFilter.LengthFilter(20))
                binding.layoutCreateShort.tvCountChar.text = "0/20"
                binding.layoutCreateShort.edtContent.checkOnEditTextChange {
                    runOnUiThread {
                        binding.layoutCreateShort.tvCountChar.text = "$it/20"
                    }
                    setOnFocus(
                        binding.layoutCreateShort.tvEmpty,
                        binding.layoutCreateShort.cvContent
                    )
                }
                binding.layoutCreate.tap {
                    loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                        onNextAction = {
                            if (binding.layoutCreateShort.edtContent.text.toString().trim().isEmpty()) {
                                showError(
                                    binding.layoutCreateShort.tvEmpty,
                                    binding.layoutCreateShort.cvContent,
                                    getString(R.string.please_enter_content)
                                )
                            }
                            if (binding.layoutCreateShort.edtContent.text.toString().trim().isNotEmpty()) {
                                val bundle = Bundle().apply {
                                    putString(
                                        Constants.ScreenKey.CONTENT_TEXT,
                                        binding.layoutCreateShort.edtContent.text.toString().trim()
                                    )
                                }
                                goToResultActivity(bundle, Type.BARCODE.toString(), TypeUI.SINGLE)
                            }
                        }
                    )
                }
            }

            Type.FACEBOOK.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.facebook)
                binding.layoutCreateShort.edtContent.filters = arrayOf(InputFilter.LengthFilter(1000))
                goToResultFromSocial(
                    getString(R.string.facebook),
                    "https://www.facebook.com/",
                    Type.FACEBOOK
                )
            }

            Type.INSTAGRAM.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.instagram)
                binding.layoutCreateShort.edtContent.filters = arrayOf(InputFilter.LengthFilter(1000))
                goToResultFromSocial(
                    getString(R.string.instagram),
                    "https://www.instagram.com/",
                    Type.INSTAGRAM
                )
            }

            Type.YOUTUBE.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.youtube)
                binding.layoutCreateShort.edtContent.filters = arrayOf(InputFilter.LengthFilter(1000))
                goToResultFromSocial(
                    getString(R.string.youtube),
                    "https://www.youtube.com/",
                    Type.YOUTUBE
                )
            }

            Type.TWITTER.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.twitter)
                binding.layoutCreateShort.edtContent.filters = arrayOf(InputFilter.LengthFilter(1000))
                goToResultFromSocial(
                    getString(R.string.twitter),
                    "https://www.twitter.com/",
                    Type.TWITTER
                )
            }

            Type.WHATSAPP.toString() -> {
                binding.tvTypeCreate.text = getString(R.string.whats_app)
                binding.layoutCreateShort.edtContent.filters = arrayOf(InputFilter.LengthFilter(1000))
                goToResultFromSocial(
                    getString(R.string.whats_app),
                    "https://www.whatsapp.com/",
                    Type.WHATSAPP
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun goToResultFromSocial(typeName: String, default: String, type: Type) {
        binding.layoutCreateShort.tvType.text = typeName
        binding.layoutCreateShort.edtContent.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.layoutCreateShort.edtContent.hint = default
        binding.layoutCreateShort.tvCountChar.text =
            "${binding.layoutCreateShort.edtContent.text.toString().length}/1000"
        binding.layoutCreateShort.edtContent.checkOnEditTextChange {
            runOnUiThread {
                binding.layoutCreateShort.tvCountChar.text = "$it/1000"
            }
            setOnFocus(binding.layoutCreateShort.tvEmpty, binding.layoutCreateShort.cvContent)
        }
        binding.layoutCreate.tap {
            loadAndShowInter(Constants.RemoteKeys.inter_create, Constants.RemoteKeys.inter_create,
                onNextAction = {
                    when (type) {
                        Type.YOUTUBE -> {
                            Log.i("tag", binding.layoutCreateShort.edtContent.text.toString().trim())
                            if (!isYouTubeUrl(binding.layoutCreateShort.edtContent.text.toString().trim())) {
                                showError(binding.layoutCreateShort.tvEmpty, binding.layoutCreateShort.cvContent, getString(R.string.invalid_youtube_url))
                            } else if (binding.layoutCreateShort.edtContent.text.toString().trim().isEmpty()) {
                                showError(
                                    binding.layoutCreateShort.tvEmpty,
                                    binding.layoutCreateShort.cvContent,
                                    getString(R.string.please_enter_content)
                                )
                            } else {
                                val bundle = Bundle().apply {
                                    putString(
                                        Constants.ScreenKey.CONTENT_URL,
                                        binding.layoutCreateShort.edtContent.text.toString().trim()
                                    )
                                }
                                goToResultActivity(bundle, type.toString(), TypeUI.SINGLE)
                            }
                        }

                        Type.FACEBOOK -> {
                            if (!isFacebookUrl(binding.layoutCreateShort.edtContent.text.toString().trim())) {
                                showError(binding.layoutCreateShort.tvEmpty, binding.layoutCreateShort.cvContent, getString(R.string.invalid_fb_url))
                            } else if (binding.layoutCreateShort.edtContent.text.toString().trim().isEmpty()) {
                                showError(
                                    binding.layoutCreateShort.tvEmpty,
                                    binding.layoutCreateShort.cvContent,
                                    getString(R.string.please_enter_content)
                                )
                            } else {
                                val bundle = Bundle().apply {
                                    putString(
                                        Constants.ScreenKey.CONTENT_URL,
                                        binding.layoutCreateShort.edtContent.text.toString().trim()
                                    )
                                }
                                goToResultActivity(bundle, type.toString(), TypeUI.SINGLE)
                            }
                        }

                        Type.WHATSAPP -> {
                            if (!isWhatsappUrl(binding.layoutCreateShort.edtContent.text.toString().trim())) {
                                showError(binding.layoutCreateShort.tvEmpty, binding.layoutCreateShort.cvContent, getString(R.string.invalid_whatsapp_url))
                            } else if (binding.layoutCreateShort.edtContent.text.toString().trim().isEmpty()) {
                                showError(
                                    binding.layoutCreateShort.tvEmpty,
                                    binding.layoutCreateShort.cvContent,
                                    getString(R.string.please_enter_content)
                                )
                            } else {
                                val bundle = Bundle().apply {
                                    putString(
                                        Constants.ScreenKey.CONTENT_URL,
                                        binding.layoutCreateShort.edtContent.text.toString().trim()
                                    )
                                }
                                goToResultActivity(bundle, type.toString(), TypeUI.SINGLE)
                            }
                        }

                        Type.TWITTER -> {
                            if (!isTwitterUrl(binding.layoutCreateShort.edtContent.text.toString().trim())) {
                                showError(binding.layoutCreateShort.tvEmpty, binding.layoutCreateShort.cvContent, getString(R.string.invalid_twitter_url))
                            } else if (binding.layoutCreateShort.edtContent.text.toString().trim().isEmpty()) {
                                showError(
                                    binding.layoutCreateShort.tvEmpty,
                                    binding.layoutCreateShort.cvContent,
                                    getString(R.string.please_enter_content)
                                )
                            } else {
                                val bundle = Bundle().apply {
                                    putString(
                                        Constants.ScreenKey.CONTENT_URL,
                                        binding.layoutCreateShort.edtContent.text.toString().trim()
                                    )
                                }
                                goToResultActivity(bundle, type.toString(), TypeUI.SINGLE)
                            }
                        }

                        Type.INSTAGRAM -> {
                            if (!isInstagramUrl(binding.layoutCreateShort.edtContent.text.toString().trim())) {
                                showError(binding.layoutCreateShort.tvEmpty, binding.layoutCreateShort.cvContent, getString(R.string.invalid_instagram_url))
                            } else if (binding.layoutCreateShort.edtContent.text.toString().trim().isEmpty()) {
                                showError(
                                    binding.layoutCreateShort.tvEmpty,
                                    binding.layoutCreateShort.cvContent,
                                    getString(R.string.please_enter_content)
                                )
                            } else {
                                val bundle = Bundle().apply {
                                    putString(
                                        Constants.ScreenKey.CONTENT_URL,
                                        binding.layoutCreateShort.edtContent.text.toString().trim()
                                    )
                                }
                                goToResultActivity(bundle, type.toString(), TypeUI.SINGLE)
                            }
                        }

                        else -> {

                        }
                    }
                }
            )
        }
    }

    private fun goToResultActivity(bundle: Bundle, type: String, typeUI: TypeUI) {
        bundle.apply {
            putString(KEY_SCREEN_TO_RESULT, "create")
            putString(Constants.ScreenKey.CODE_TYPE, type)
            putString(Constants.ScreenKey.UI_CODE_TYPE, typeUI.toString())
            putString(Constants.ScreenKey.TIME_HOUR, getCurrentHHMM())
            putString(Constants.ScreenKey.TIME_DATE, getCurrentDate())
        }
        val intent = Intent(this@CreateNo1Activity, ScanResultNo1Activity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    override fun initData() {
        screenKey = intent?.extras?.getString(KEY_SCREEN_INTO_CREATE).toString()
        Log.i("key", screenKey.toString())
        listShort.add(Type.PHONE.toString())
        listShort.add(Type.BARCODE.toString())
        listShort.add(Type.FACEBOOK.toString())
        listShort.add(Type.INSTAGRAM.toString())
        listShort.add(Type.TWITTER.toString())
        listShort.add(Type.WHATSAPP.toString())
        listShort.add(Type.YOUTUBE.toString())

        listLong.add(Type.TEXT.toString())
        listLong.add(Type.URL.toString())

        listMultiple.add(Type.WIFI.toString())
        listMultiple.add(Type.EMAIL.toString())
        listMultiple.add(Type.SMS.toString())
        listMultiple.add(Type.LOCATION.toString())
        listMultiple.add(Type.CONTACT.toString())
    }

    override fun onBackPressCustom() {
        finish()
    }

    override fun getViewBinding(): ActivityCreateNo1Binding {
        return ActivityCreateNo1Binding.inflate(layoutInflater)
    }

    override fun onPause() {
        super.onPause()
        Log.i("life", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("life", "onStop")
    }


}
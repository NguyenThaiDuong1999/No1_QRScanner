package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.result_scan

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.AppDatabaseNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.HistoryModel
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.HistoryType
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityScanResultNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.widget.AppWidgetPinnedReceiverNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.widget.CodeWidgetProviderNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.Type
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.TypeUI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.result_scan.template.TemplateScanResultAdapterNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.scan.ScanNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.TemplateNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.background.BackgroundModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.color.ColorModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.logo.LogoModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.template_sub.TemplateModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper.SharePrefHelper
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.IntentKey.BACKGROUND
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.IntentKey.CHANGE_TEMPLATE_DONE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.IntentKey.COLOR
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.IntentKey.LOGO
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.IntentKey.TEMPLATE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_CONTACT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_EMAIL
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_LOCATION
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_PHONE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_SMS
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_TEXT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_URL
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_WIFI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.KEY_SCREEN_TO_RESULT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.TIME_DATE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.TIME_HOUR
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.callNow
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.checkExternalStoragePermission
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.clearCacheDirectory
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.copyText
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.generate128Barcode
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.generateGeoQRCode
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.generateQRCode
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.getBitmapFromView
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.goToSettings
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isLGDevice
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isLargerTiramisu
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.logI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.openMap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.openUrl
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.openWebSearch
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.openWifiSetting
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.sendEmail
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.sendSMS
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.shareCode
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.shareCodeIfSavedToCache
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.shareText
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.showRationaleDialog
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.startSaveBitmapAsImageToCache
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.startSaveBitmapAsImageToStorage
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.toast
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class ScanResultNo1Activity : BaseNo1Activity<ActivityScanResultNo1Binding>() {

    private var codeType = ""
    private var uiCodeType = ""
    private var timeHour = ""
    private var timeDate = ""
    private var keyScreen = ""
    private var tempKey = ""
    private var listTemplate = mutableListOf<TemplateModelNo1>()
    private var templateScanResultAdapterNo1: TemplateScanResultAdapterNo1? = null
    private var backgroundModelNo1: BackgroundModelNo1? = null
    private var colorModelNo1: ColorModelNo1? = null
    private var logoModelNo1: LogoModelNo1? = null
    private var templateModelNo1: TemplateModelNo1? = null
    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == CHANGE_TEMPLATE_DONE) {
                backgroundModelNo1 = p1.getSerializableExtra(BACKGROUND) as BackgroundModelNo1?
                colorModelNo1 = p1.getSerializableExtra(COLOR) as ColorModelNo1?
                logoModelNo1 = p1.getSerializableExtra(LOGO) as LogoModelNo1?
                templateModelNo1 = p1.getSerializableExtra(TEMPLATE) as TemplateModelNo1?
                if (templateModelNo1 != null) {
                    templateModelNo1?.templateImage?.let {
                        binding.rlTemplate.setBackgroundResource(it)
                    }
                    templateScanResultAdapterNo1?.selectItem(templateModelNo1!!)
                } else {
                    binding.rlTemplate.setBackgroundColor(Color.WHITE)
                    templateScanResultAdapterNo1?.unSelectAllItem()
                }
                if (myTemplateBitmap != null) {
                    binding.rlTemplate.background = BitmapDrawable(resources, myTemplateBitmap)
                    templateScanResultAdapterNo1?.unSelectAllItem()
                }
                backgroundModelNo1?.backgroundImage?.let {
                    binding.rlQr.setBackgroundResource(it)
                }
                colorModelNo1?.colorImage?.let {
                    binding.ivCodeCreate.setColorFilter(Color.parseColor(it), PorterDuff.Mode.SRC_IN)
                }
                logoModelNo1?.logoImage?.let { binding.imgLogo.setImageResource(it) }
            }
        }
    }

    companion object {
        var myQRBitmap: Bitmap? = null
        var myTemplateBitmap: Bitmap? = null
    }

    private fun loadNativeResult() {
        loadNative(
            binding.frAds,
            Constants.RemoteKeys.native_result,
            Constants.RemoteKeys.native_result,
            Constants.RemoteKeys.native_backup,
            R.layout.native_language_custom,
            R.layout.shimmer_native_lang_custom
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        loadNativeResult()
        loadCollapseBanner(Constants.RemoteKeys.collapse_result)

        LocalBroadcastManager.getInstance(this@ScanResultNo1Activity).registerReceiver(broadcastReceiver, IntentFilter(CHANGE_TEMPLATE_DONE))
        if (isLGDevice()) {
            binding.layoutShareCreateLg.visible()
            binding.layoutActionCreate.gone()
        } else {
            binding.layoutShareCreateLg.gone()
            binding.layoutActionCreate.visible()
        }

        codeType = intent.extras?.getString(Constants.ScreenKey.CODE_TYPE).toString()
        uiCodeType = intent.extras?.getString(Constants.ScreenKey.UI_CODE_TYPE).toString()
        timeHour = intent.extras?.getString(TIME_HOUR).toString()
        timeDate = intent.extras?.getString(TIME_DATE).toString()
        keyScreen = intent.extras?.getString(KEY_SCREEN_TO_RESULT).toString()
        tempKey = keyScreen
        if (keyScreen == "create") {
            binding.cvTemplate.visible()
        } else {
            binding.cvTemplate.gone()
        }
        binding.icHome.tap {
            startActivity(Intent(this, MainNo1Activity::class.java))
            finishAffinity()
        }
        binding.icBack.tap {
            when (keyScreen) {
                "scan" -> {
                    startActivity(Intent(this@ScanResultNo1Activity, ScanNo1Activity::class.java))
                    finish()
                }

                "create", "batch", "history" -> finish()
            }
        }
        when (uiCodeType) {
            TypeUI.SINGLE.toString() -> {
                binding.tvQrContent.visible()
                binding.rcvContent.gone()
                binding.tvTimeHour.gone()
                binding.tvTimeDate.gone()
            }

            TypeUI.MULTIPLE.toString() -> {
                binding.tvQrContent.gone()
                binding.rcvContent.visible()
                binding.tvTimeHour.gone()
                binding.tvTimeDate.gone()
            }

            TypeUI.BARCODE.toString() -> {
                binding.tvQrContent.visible()
                binding.tvTimeHour.visible()
                binding.tvTimeDate.visible()
                binding.rcvContent.gone()
            }
        }
        if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
            binding.layoutScanResultAction.visible()
            binding.layoutCreateResultAction.gone()
        } else if (keyScreen == "create") {
            binding.layoutScanResultAction.gone()
            binding.layoutCreateResultAction.visible()
        }
        when (codeType) {
            Type.TEXT.toString() -> {
                val content = intent?.extras?.getString(CONTENT_TEXT).toString()
                Log.i("yyy", content)
                binding.tvType.text = getString(R.string.text)
                binding.tvQrContent.text = content
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_search,
                        getString(R.string.web_search),
                        content,
                        Type.TEXT,
                        System.currentTimeMillis().toString(),
                        getString(R.string.text)
                    ) {

                        openWebSearch(content)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.text),
                        Type.TEXT,
                    )
                }
            }

            Type.URL.toString() -> {
                val content = intent?.extras?.getString(CONTENT_URL).toString()
                binding.tvQrContent.text = content
                Log.i("url", content)
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_url,
                        getString(R.string.open_url),
                        content,
                        Type.URL,
                        System.currentTimeMillis().toString(),
                        getString(R.string.website)
                    ) {
                        openUrl(content)
                    }
                } else if (keyScreen == "create") {
                    binding.tvQrContent.text = content
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.website),
                        Type.URL,
                    )
                }
            }

            Type.WIFI.toString() -> {
                val listContent = mutableListOf<String>()
                val contentWifi = intent?.extras?.getString(CONTENT_WIFI).toString()
                Log.i("wifi", contentWifi)
                val securityRegex = Regex("T:(.*?);")
                val nameRegex = Regex("S:(.*?);")
                val passwordRegex = Regex("P:(.*?);")

                val securityMatch = securityRegex.find(contentWifi)
                val nameMatch = nameRegex.find(contentWifi)
                val passwordMatch = passwordRegex.find(contentWifi)

                val security = securityMatch?.groups?.get(1)?.value.toString()
                val name = nameMatch?.groups?.get(1)?.value.toString()
                val password = passwordMatch?.groups?.get(1)?.value.toString()

                listContent.add("${getString(R.string.wifi)}:$name")
                listContent.add("${getString(R.string.password)}:$password")
                listContent.add(
                    "${getString(R.string.security)}:${
                        if (security == "nopass") getString(
                            R.string.none
                        ) else security
                    }"
                )

                binding.tvType.text = getString(R.string.wifi)
                binding.rcvContent.adapter = MultipleContentAdapterNo1(listContent)
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.wifi,
                        getString(R.string.connect),
                        contentWifi,
                        Type.WIFI,
                        System.currentTimeMillis().toString(),
                        getString(R.string.wifi)
                    ) {
                        openWifiSetting()
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        contentWifi,
                        System.currentTimeMillis().toString(),
                        getString(R.string.wifi),
                        Type.WIFI,
                    )
                }
            }

            Type.EMAIL.toString() -> {
                val list = mutableListOf<String>()
                binding.tvType.text = getString(R.string.email)
                var address = ""
                var subject = ""
                var body = ""
                val content = intent.extras?.getString(CONTENT_EMAIL).toString().trimIndent()
                logI("email", content)
                val contentParse = content.removePrefix("MATMSG:")
                val toRegex = "TO:(.*?);SUB:".toRegex()
                val subRegex = "SUB:(.*?);BODY:".toRegex()
                val bodyRegex = "BODY:(.*?);{0,2}$".toRegex()

                val toMatch = toRegex.find(contentParse)
                val subMatch = subRegex.find(contentParse)
                val bodyMatch = bodyRegex.find(contentParse)
                if (toMatch != null) {
                    address = toMatch.groupValues[1].trim()
                }
                if (subMatch != null) {
                    subject = subMatch.groupValues[1].trim()
                }
                if (bodyMatch != null) {
                    body = bodyMatch.groupValues[1].trim()
                }
                list.add("${getString(R.string.email)}:$address")
                list.add("${getString(R.string.title)}:$subject")
                list.add("${getString(R.string.content)}:$body")
                binding.rcvContent.adapter = MultipleContentAdapterNo1(list)
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_email_result,
                        getString(R.string.send_email),
                        content,
                        Type.EMAIL,
                        System.currentTimeMillis().toString(),
                        getString(R.string.email)
                    ) {

                        sendEmail(address, subject, body)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.email),
                        Type.EMAIL
                    )
                }
            }

            Type.SMS.toString() -> {
                val list = mutableListOf<String>()
                val content = intent.extras?.getString(CONTENT_SMS).toString()
                val contentParse = if (content.contains("smsto:")) {
                    content.removePrefix("smsto:")
                } else {
                    content.removePrefix("SMSTO:")
                }
                Log.i("sms", content)
                Log.i("sms_p", contentParse)
                val parts = contentParse.split(":", limit = 2)
                val phoneNumber = parts[0].trim()
                val msg = parts[1].trim()
                list.add("${getString(R.string.phone_number)}:$phoneNumber")
                list.add("${getString(R.string.message)}:$msg")
                binding.tvType.text = getString(R.string.sms)
                binding.rcvContent.adapter = MultipleContentAdapterNo1(list)
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_send_sms,
                        getString(R.string.send_sms),
                        content,
                        Type.SMS,
                        System.currentTimeMillis().toString(),
                        getString(R.string.sms)
                    ) {

                        sendSMS(phoneNumber, msg)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.sms),
                        Type.SMS,
                    )
                }
            }

            Type.PHONE.toString() -> {
                binding.tvType.text = getString(R.string.phone)
                val content = intent.extras?.getString(CONTENT_PHONE).toString()
                val phoneNumber = intent.extras?.getString(CONTENT_PHONE)?.split(":", limit = 2)
                binding.tvQrContent.text = phoneNumber!![1].trim()
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_phone_result,
                        getString(R.string.call_now),
                        phoneNumber[1].trim(),
                        Type.PHONE,
                        System.currentTimeMillis().toString(),
                        getString(R.string.phone)
                    ) {

                        callNow(phoneNumber[1].trim())
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.phone),
                        Type.PHONE,
                    )
                }
            }

            Type.LOCATION.toString() -> {
                val content = intent.extras?.getString(CONTENT_LOCATION).toString()
                Log.i("mmm", content)
                val contentLogic = if (content.contains("geo:")) {
                    content.removePrefix("geo:")
                } else {
                    content.removePrefix("GEO:")
                }
                val list = mutableListOf<String>()
                val lat = contentLogic.split(",")[0].trim()
                val lng = contentLogic.split(",")[1].trim()
                list.add("${getString(R.string.latitude)}:$lat")
                list.add("${getString(R.string.longitude)}:$lng")
                binding.tvType.text = getString(R.string.position)
                binding.rcvContent.adapter = MultipleContentAdapterNo1(list)
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_location_result,
                        getString(R.string.show_on_map),
                        content,
                        Type.LOCATION,
                        System.currentTimeMillis().toString(),
                        getString(R.string.location)
                    ) {

                        openMap(lat, lng)
                    }
                } else {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.location),
                        Type.LOCATION,
                    )
                }
            }

            Type.CONTACT.toString() -> {
                val content = intent.extras?.getString(CONTENT_CONTACT).toString()
                Log.i("contact", content)
                val lines = content.lines()
                var name = ""
                var email = ""
                var phone = ""
                var job = ""
                var company = ""
                var address = ""
                var addShown = ""
                var note = ""
                for (line in lines) {
                    when {
                        line.startsWith("FN:") -> {
                            name = line.substringAfter("FN:").replace(";", "")
                        }

                        line.startsWith("EMAIL;WORK;INTERNET:") -> {
                            email = line.substringAfter("EMAIL;WORK;INTERNET:")
                        }

                        line.startsWith("TEL;CELL:") -> {
                            phone = line.substringAfter("TEL;CELL:")
                        }

                        line.startsWith("TITLE:") -> {
                            job = line.substringAfter("TITLE:")
                        }

                        line.startsWith("ORG:") -> {
                            company = line.substringAfter("ORG:")
                        }

                        line.startsWith("ADR:") -> {
                            address =
                                line.substringAfter("ADR:").replace(";", "?").replace(",", "?")
                            val listAdd = address.split("?")
                            val listShown = mutableListOf<String>()
                            for (str in listAdd) {
                                if (str.isNotEmpty()) {
                                    listShown.add(str)
                                }
                            }
                            for (i in 0 until listShown.size) {
                                addShown += if (i == listShown.size - 1) {
                                    listShown[i]
                                } else {
                                    "${listShown[i]}, "
                                }
                            }
                        }

                        line.startsWith("URL:") -> {
                            note = line.substringAfter("URL:")
                        }
                    }
                }
                val list = mutableListOf<String>()
                list.add("${getString(R.string.name)}:$name")
                list.add("${getString(R.string.email)}:$email")
                list.add("${getString(R.string.phone)}:$phone")
                list.add("${getString(R.string.job_title)}:$job")
                list.add("${getString(R.string.company)}:$company")
                list.add("${getString(R.string.address)}:$addShown")
                list.add("${getString(R.string.note)}:$note")
                binding.tvType.text = getString(R.string.contact)
                binding.rcvContent.adapter = MultipleContentAdapterNo1(list)
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_phone_result,
                        getString(R.string.call_now),
                        content,
                        Type.CONTACT,
                        System.currentTimeMillis().toString(),
                        getString(R.string.contact)
                    ) {

                        callNow(phone)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.contact),
                        Type.CONTACT,
                    )
                }
            }

            Type.BARCODE.toString() -> {
                val content = intent.extras?.getString(CONTENT_TEXT).toString()
                Log.i("barcode", content)
                val contentTimeHour = intent.extras?.getString(TIME_HOUR)
                val contentTimeDate = intent.extras?.getString(TIME_DATE)
                binding.tvType.text = getString(R.string.bar_code)
                binding.tvQrContent.text = content
                binding.tvTimeDate.visible()
                binding.tvTimeHour.visible()
                binding.tvTimeHour.text = contentTimeHour
                binding.tvTimeDate.text = contentTimeDate
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_search,
                        getString(R.string.web_search),
                        content,
                        Type.BARCODE,
                        System.currentTimeMillis().toString(),
                        getString(R.string.bar_code)
                    ) {

                        openWebSearch(content)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.bar_code),
                        Type.BARCODE,
                    )
                }
            }

            Type.FACEBOOK.toString() -> {
                val content = intent?.extras?.getString(CONTENT_URL).toString()
                binding.tvQrContent.text = content
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_url,
                        getString(R.string.open_url),
                        content,
                        Type.FACEBOOK,
                        System.currentTimeMillis().toString(),
                        getString(R.string.facebook)
                    ) {
                        openUrl(content)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.facebook),
                        Type.FACEBOOK,
                    )
                }
            }

            Type.YOUTUBE.toString() -> {
                val content = intent?.extras?.getString(CONTENT_URL).toString()
                binding.tvQrContent.text = content
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_url,
                        getString(R.string.open_url),
                        content,
                        Type.YOUTUBE,
                        System.currentTimeMillis().toString(),
                        getString(R.string.youtube)
                    ) {
                        openUrl(content)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.youtube),
                        Type.YOUTUBE,
                    )
                }
            }

            Type.INSTAGRAM.toString() -> {
                val content = intent?.extras?.getString(CONTENT_URL).toString()
                binding.tvQrContent.text = content
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_url,
                        getString(R.string.open_url),
                        content,
                        Type.INSTAGRAM,
                        System.currentTimeMillis().toString(),
                        getString(R.string.instagram)
                    ) {
                        openUrl(content)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.instagram),
                        Type.INSTAGRAM,
                    )
                }
            }

            Type.WHATSAPP.toString() -> {
                val content = intent?.extras?.getString(CONTENT_URL).toString()
                binding.tvQrContent.text = content
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_url,
                        getString(R.string.open_url),
                        content,
                        Type.WHATSAPP,
                        System.currentTimeMillis().toString(),
                        getString(R.string.whats_app)
                    ) {
                        openUrl(content)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.whats_app),
                        Type.WHATSAPP,
                    )
                }
            }

            Type.TWITTER.toString() -> {
                val content = intent?.extras?.getString(CONTENT_URL).toString()
                binding.tvQrContent.text = content
                if (keyScreen == "scan" || keyScreen == "batch" || keyScreen == "history") {
                    showNextActAfterScan(
                        R.drawable.ic_url,
                        getString(R.string.open_url),
                        content,
                        Type.TWITTER,
                        System.currentTimeMillis().toString(),
                        getString(R.string.twitter)
                    ) {
                        openUrl(content)
                    }
                } else if (keyScreen == "create") {
                    showNextActAfterCreate(
                        content,
                        System.currentTimeMillis().toString(),
                        getString(R.string.twitter),
                        Type.TWITTER,
                    )
                }
            }
        }

        //Template
        addListTemplate()
        templateScanResultAdapterNo1 = TemplateScanResultAdapterNo1(this, listTemplate,
            onClickItem = {
                myTemplateBitmap = null
                templateModelNo1 = it
                it.templateImage?.let { it1 -> binding.rlTemplate.setBackgroundResource(it1) }
            }
        )
        val layoutManager = GridLayoutManager(this@ScanResultNo1Activity, 4)
        binding.rcvTemplate.layoutManager = layoutManager
        binding.rcvTemplate.adapter = templateScanResultAdapterNo1

        binding.tvMore.tap {
            val intent = Intent(this, TemplateNo1Activity::class.java)
            intent.putExtra(BACKGROUND, backgroundModelNo1)
            intent.putExtra(COLOR, colorModelNo1)
            intent.putExtra(LOGO, logoModelNo1)
            intent.putExtra(TEMPLATE, templateModelNo1)
            launchActivityIntent(intent)
        }
        binding.rlQr.setBackgroundResource(R.drawable.ic_background_white_template)
    }

    private fun addListTemplate() {
        listTemplate.add(TemplateModelNo1(R.drawable.img_template7))
        listTemplate.add(TemplateModelNo1(R.drawable.img_template2))
        listTemplate.add(TemplateModelNo1(R.drawable.img_template8))
        listTemplate.add(TemplateModelNo1(R.drawable.img_template5))
    }


    private fun saveHistory(type: Type, content: String, timeDone: String) {
        AppDatabaseNo1.getInstance(this).appDao().insertHistoryModel(
            HistoryModel(
                id = 0,
                codeType = type.toString(),
                uiType = uiCodeType,
                historyType = if (keyScreen == "scan") HistoryType.SCANNED.toString() else HistoryType.CREATED.toString(),
                contentRawValue = content,
                timeHour = timeHour,
                timeDate = timeDate,
                timeDone = timeDone
            )
        )
    }

    private fun showNextActAfterScan(
        imgNextAct: Int,
        tvNextAct: String,
        content: String,
        type: Type,
        timeDone: String,
        typeName: String,
        onNextAction: () -> Unit
    ) {
        Log.i("hhhh", content)
        if (keyScreen != "history") {
            saveHistory(type, content, timeDone)
        }
        binding.ivNextActAfterScan.setImageResource(imgNextAct)
        binding.tvNextActAfterScan.text = tvNextAct
        binding.tvType.text = typeName
        binding.tvNextActAfterScan.tap {
            onNextAction.invoke()
        }
        val bitmap = when (type) {
            Type.BARCODE -> {
                generate128Barcode(content)
            }

            Type.LOCATION -> {
                generateGeoQRCode(content, 512, 512)
            }

            Type.PHONE -> {
                Log.i("bbb", content)
                generateGeoQRCode(content, 512, 512)
            }

            else -> {
                generateQRCode(content, 512, 512)
            }
        }
        binding.ivCodeScan.setImageBitmap(bitmap)
        binding.layoutShareScan.tap {
            shareText(content)
        }
        binding.layoutCopyScan.tap {
            copyText("", content)
        }
    }


    private fun showNextActAfterCreate(
        content: String,
        timeDone: String,
        typeName: String,
        type: Type,
    ) {
        binding.title.text = getString(R.string.result)
        binding.tvType.text = typeName
        if (keyScreen != "history") {
            saveHistory(type, content, timeDone)
        }
        val bitmap = when (type) {
            Type.BARCODE -> {
                generate128Barcode(content)
            }

            Type.LOCATION -> {
                generateGeoQRCode(content, 512, 512)
            }

            Type.PHONE -> {
                Log.i("bbb", content)
                generateGeoQRCode(content, 512, 512)
            }

            else -> {
                generateQRCode(content, 512, 512)
            }
        }
        myQRBitmap = bitmap
        binding.ivCodeCreate.setImageBitmap(bitmap)
        binding.layoutShareCreate.tap {
            startSaveBitmapAsImageToCache(if (keyScreen == "create") binding.cvQr.getBitmapFromView() else bitmap, timeDone,
                onSuccess = {

                    shareCode(it)
                },
                onSaved = {
                    shareCodeIfSavedToCache(timeDone)
                })
        }
        binding.layoutShareCreateLg.tap {
            startSaveBitmapAsImageToCache(if (keyScreen == "create") binding.cvQr.getBitmapFromView() else bitmap, timeDone,
                onSuccess = {

                    shareCode(it)
                },
                onSaved = {

                    shareCodeIfSavedToCache(timeDone)
                })
        }
        binding.layoutSaveQr.tap {
            checkExternalStoragePermission(
                onPermissionGranted = {
                    startSaveBitmapAsImageToStorage(if (keyScreen == "create") binding.cvQr.getBitmapFromView() else bitmap, timeDone,
                        onSuccess = {
                            toast("${getString(R.string.image_was_saved)}. ${getString(R.string.file_path)}:$timeDone.png")
                        },
                        onSaved = {
                            runOnUiThread {
                                toast("${getString(R.string.image_was_saved)}. ${getString(R.string.file_path)}:$it")
                            }
                        })
                },
                onShowRationale = {
                    showRationaleDialog(getString(R.string.storage),
                        onGoToSetting = {

                            goToSettings()
                        })
                },
                onRequestPermission = {

                }
            )


        }
        binding.layoutAddWidget.tap {
            val appWidgetManager =
                AppWidgetManager.getInstance(this@ScanResultNo1Activity)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && appWidgetManager.isRequestPinAppWidgetSupported) {
                checkExternalStoragePermission(
                    onPermissionGranted = {
                        startSaveBitmapAsImageToStorage(if (keyScreen == "create") binding.cvQr.getBitmapFromView() else bitmap, timeDone,
                            onSuccess = { path ->
                                Log.i("widget", path)
                                SharePrefHelper(this).saveString("widget", path)
                                val componentName =
                                    ComponentName(this, CodeWidgetProviderNo1::class.java)
                                val callback = PendingIntent.getBroadcast(
                                    this@ScanResultNo1Activity,
                                    0,
                                    Intent(this, AppWidgetPinnedReceiverNo1::class.java),
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )
                                appWidgetManager.requestPinAppWidget(
                                    componentName,
                                    null,
                                    callback
                                )
                                Toast.makeText(this@ScanResultNo1Activity, R.string.add_widget_successfully, Toast.LENGTH_SHORT).show()
                            },
                            onSaved = {
                                SharePrefHelper(this).saveString("widget", it)
                                val componentName =
                                    ComponentName(this, CodeWidgetProviderNo1::class.java)
                                val callback = PendingIntent.getBroadcast(
                                    this,
                                    0,
                                    Intent(this, AppWidgetPinnedReceiverNo1::class.java),
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )
                                appWidgetManager.requestPinAppWidget(
                                    componentName,
                                    null,
                                    callback
                                )
                            })
                    },
                    onShowRationale = {
                        showRationaleDialog(
                            if (isLargerTiramisu()) getString(R.string.this_app_need_read_images_permission_to_enhance_feature)
                            else getString(R.string.this_app_need_external_storage_to_enhance_feature)
                        ) {

                            goToSettings()
                        }
                    },
                    onRequestPermission = {

                    }
                )
            } else {
                toast(getString(R.string.this_device_no_support))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        tempKey = ""
        Log.i("life", "stop")
    }

    override fun onResume() {
        super.onResume()
        Log.i("life", "resume")
        clearCacheDirectory(this, "scan_qr")
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        when (keyScreen) {
            "scan" -> {
                startActivity(Intent(this@ScanResultNo1Activity, ScanNo1Activity::class.java))
                finish()
            }

            "create", "batch", "history" -> finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myTemplateBitmap = null
        LocalBroadcastManager.getInstance(this@ScanResultNo1Activity).unregisterReceiver(broadcastReceiver)
        Log.i("life", "destroy")
    }

    override fun onPause() {
        super.onPause()
        Log.i("life", "pause")
    }


    override fun getViewBinding(): ActivityScanResultNo1Binding {
        return ActivityScanResultNo1Binding.inflate(layoutInflater)
    }

}
package com.scanqr.qrscanner.qrgenerator.feature.result_scan

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
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.database.AppDatabase
import com.scanqr.qrscanner.qrgenerator.database.HistoryModel
import com.scanqr.qrscanner.qrgenerator.database.HistoryType
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityScanResultBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainActivity
import com.scanqr.qrscanner.qrgenerator.feature.main.create.widget.AppWidgetPinnedReceiver
import com.scanqr.qrscanner.qrgenerator.feature.main.create.widget.CodeWidgetProvider
import com.scanqr.qrscanner.qrgenerator.feature.main.model.Type
import com.scanqr.qrscanner.qrgenerator.feature.main.model.TypeUI
import com.scanqr.qrscanner.qrgenerator.feature.result_scan.template.TemplateScanResultAdapter
import com.scanqr.qrscanner.qrgenerator.feature.scan.ScanActivity
import com.scanqr.qrscanner.qrgenerator.feature.template.TemplateActivity
import com.scanqr.qrscanner.qrgenerator.feature.template.background.BackgroundModel
import com.scanqr.qrscanner.qrgenerator.feature.template.color.ColorModel
import com.scanqr.qrscanner.qrgenerator.feature.template.logo.LogoModel
import com.scanqr.qrscanner.qrgenerator.feature.template.template_sub.TemplateModel
import com.scanqr.qrscanner.qrgenerator.helper.SharePrefHelper
import com.scanqr.qrscanner.qrgenerator.utils.Constants
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.BACKGROUND
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.CHANGE_TEMPLATE_DONE
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.COLOR
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.LOGO
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.TEMPLATE
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_CONTACT
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_EMAIL
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_LOCATION
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_PHONE
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_SMS
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_TEXT
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_URL
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_WIFI
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.KEY_SCREEN_TO_RESULT
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.TIME_DATE
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.TIME_HOUR
import com.scanqr.qrscanner.qrgenerator.utils.callNow
import com.scanqr.qrscanner.qrgenerator.utils.checkExternalStoragePermission
import com.scanqr.qrscanner.qrgenerator.utils.clearCacheDirectory
import com.scanqr.qrscanner.qrgenerator.utils.copyText
import com.scanqr.qrscanner.qrgenerator.utils.generate128Barcode
import com.scanqr.qrscanner.qrgenerator.utils.generateGeoQRCode
import com.scanqr.qrscanner.qrgenerator.utils.generateQRCode
import com.scanqr.qrscanner.qrgenerator.utils.getBitmapFromView
import com.scanqr.qrscanner.qrgenerator.utils.goToSettings
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.isLGDevice
import com.scanqr.qrscanner.qrgenerator.utils.isLargerTiramisu
import com.scanqr.qrscanner.qrgenerator.utils.logI
import com.scanqr.qrscanner.qrgenerator.utils.openMap
import com.scanqr.qrscanner.qrgenerator.utils.openUrl
import com.scanqr.qrscanner.qrgenerator.utils.openWebSearch
import com.scanqr.qrscanner.qrgenerator.utils.openWifiSetting
import com.scanqr.qrscanner.qrgenerator.utils.sendEmail
import com.scanqr.qrscanner.qrgenerator.utils.sendSMS
import com.scanqr.qrscanner.qrgenerator.utils.shareCode
import com.scanqr.qrscanner.qrgenerator.utils.shareCodeIfSavedToCache
import com.scanqr.qrscanner.qrgenerator.utils.shareText
import com.scanqr.qrscanner.qrgenerator.utils.showRationaleDialog
import com.scanqr.qrscanner.qrgenerator.utils.startSaveBitmapAsImageToCache
import com.scanqr.qrscanner.qrgenerator.utils.startSaveBitmapAsImageToStorage
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.toast
import com.scanqr.qrscanner.qrgenerator.utils.visible

class ScanResultActivity : BaseActivity<ActivityScanResultBinding>() {

    private var codeType = ""
    private var uiCodeType = ""
    private var timeHour = ""
    private var timeDate = ""
    private var keyScreen = ""
    private var tempKey = ""
    private var listTemplate = mutableListOf<TemplateModel>()
    private var templateScanResultAdapter: TemplateScanResultAdapter? = null
    private var backgroundModel: BackgroundModel? = null
    private var colorModel: ColorModel? = null
    private var logoModel: LogoModel? = null
    private var templateModel: TemplateModel? = null
    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == CHANGE_TEMPLATE_DONE) {
                backgroundModel = p1.getSerializableExtra(BACKGROUND) as BackgroundModel?
                colorModel = p1.getSerializableExtra(COLOR) as ColorModel?
                logoModel = p1.getSerializableExtra(LOGO) as LogoModel?
                templateModel = p1.getSerializableExtra(TEMPLATE) as TemplateModel?
                if (templateModel != null) {
                    templateModel?.templateImage?.let {
                        binding.rlTemplate.setBackgroundResource(it)
                    }
                    templateScanResultAdapter?.selectItem(templateModel!!)
                } else {
                    binding.rlTemplate.setBackgroundColor(Color.WHITE)
                    templateScanResultAdapter?.unSelectAllItem()
                }
                if (myTemplateBitmap != null) {
                    binding.rlTemplate.background = BitmapDrawable(resources, myTemplateBitmap)
                    templateScanResultAdapter?.unSelectAllItem()
                }
                backgroundModel?.backgroundImage?.let {
                    binding.rlQr.setBackgroundResource(it)
                }
                colorModel?.colorImage?.let {
                    binding.ivCodeCreate.setColorFilter(Color.parseColor(it), PorterDuff.Mode.SRC_IN)
                }
                logoModel?.logoImage?.let { binding.imgLogo.setImageResource(it) }
            }
        }
    }

    companion object {
        var myQRBitmap: Bitmap? = null
        var myTemplateBitmap: Bitmap? = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        LocalBroadcastManager.getInstance(this@ScanResultActivity).registerReceiver(broadcastReceiver, IntentFilter(CHANGE_TEMPLATE_DONE))
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
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
        binding.icBack.tap {
            when (keyScreen) {
                "scan" -> {
                    startActivity(Intent(this@ScanResultActivity, ScanActivity::class.java))
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
                binding.rcvContent.adapter = MultipleContentAdapter(listContent)
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
                binding.rcvContent.adapter = MultipleContentAdapter(list)
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
                binding.rcvContent.adapter = MultipleContentAdapter(list)
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
                binding.rcvContent.adapter = MultipleContentAdapter(list)
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
                binding.rcvContent.adapter = MultipleContentAdapter(list)
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
        templateScanResultAdapter = TemplateScanResultAdapter(this, listTemplate,
            onClickItem = {
                myTemplateBitmap = null
                templateModel = it
                it.templateImage?.let { it1 -> binding.rlTemplate.setBackgroundResource(it1) }
            }
        )
        val layoutManager = GridLayoutManager(this@ScanResultActivity, 4)
        binding.rcvTemplate.layoutManager = layoutManager
        binding.rcvTemplate.adapter = templateScanResultAdapter

        binding.tvMore.tap {
            val intent = Intent(this, TemplateActivity::class.java)
            intent.putExtra(BACKGROUND, backgroundModel)
            intent.putExtra(COLOR, colorModel)
            intent.putExtra(LOGO, logoModel)
            intent.putExtra(TEMPLATE, templateModel)
            launchActivityIntent(intent)
        }
        binding.rlQr.setBackgroundResource(R.drawable.ic_background_white_template)
    }

    private fun addListTemplate() {
        listTemplate.add(TemplateModel(R.drawable.img_template1))
        listTemplate.add(TemplateModel(R.drawable.img_template2))
        listTemplate.add(TemplateModel(R.drawable.img_template3))
        listTemplate.add(TemplateModel(R.drawable.img_template4))
    }


    private fun saveHistory(type: Type, content: String, timeDone: String) {
        AppDatabase.getInstance(this).appDao().insertHistoryModel(
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
                AppWidgetManager.getInstance(this@ScanResultActivity)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && appWidgetManager.isRequestPinAppWidgetSupported) {
                checkExternalStoragePermission(
                    onPermissionGranted = {
                        startSaveBitmapAsImageToStorage(if (keyScreen == "create") binding.cvQr.getBitmapFromView() else bitmap, timeDone,
                            onSuccess = { path ->
                                Log.i("widget", path)
                                SharePrefHelper(this).saveString("widget", path)
                                val componentName =
                                    ComponentName(this, CodeWidgetProvider::class.java)
                                val callback = PendingIntent.getBroadcast(
                                    this@ScanResultActivity,
                                    0,
                                    Intent(this, AppWidgetPinnedReceiver::class.java),
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )
                                appWidgetManager.requestPinAppWidget(
                                    componentName,
                                    null,
                                    callback
                                )
                                Toast.makeText(this@ScanResultActivity, R.string.add_widget_successfully, Toast.LENGTH_SHORT).show()
                            },
                            onSaved = {
                                SharePrefHelper(this).saveString("widget", it)
                                val componentName =
                                    ComponentName(this, CodeWidgetProvider::class.java)
                                val callback = PendingIntent.getBroadcast(
                                    this,
                                    0,
                                    Intent(this, AppWidgetPinnedReceiver::class.java),
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
                startActivity(Intent(this@ScanResultActivity, ScanActivity::class.java))
                finish()
            }

            "create", "batch", "history" -> finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myTemplateBitmap = null
        LocalBroadcastManager.getInstance(this@ScanResultActivity).unregisterReceiver(broadcastReceiver)
        Log.i("life", "destroy")
    }

    override fun onPause() {
        super.onPause()
        Log.i("life", "pause")
    }


    override fun getViewBinding(): ActivityScanResultBinding {
        return ActivityScanResultBinding.inflate(layoutInflater)
    }

}
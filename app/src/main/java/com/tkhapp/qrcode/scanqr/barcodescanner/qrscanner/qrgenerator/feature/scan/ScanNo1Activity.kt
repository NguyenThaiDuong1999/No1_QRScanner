package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.scan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.Surface
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.AppDatabaseNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.HistoryModel
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.HistoryType
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityScanNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.Type
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.TypeUI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.result_scan.ScanResultNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.scan.batch.ScanBatchResultNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper.SharePrefHelper
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper.VibrateHelperNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_CONTACT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_EMAIL
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_LOCATION
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_PHONE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_SMS
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_TEXT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_WIFI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.FACEBOOK_SIGN
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.INSTAGRAM_SIGN
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.TWITTER_SIGN
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.WHATSAPP_SIGN
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.ScreenKey.YOUTUBE_SIGN
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_OPEN_URL
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_SOUND
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_VIBRATE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.checkExternalStoragePermission
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.detectCodeInImageWithMLKit
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.getCurrentDate
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.getCurrentHHMM
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.goToSettings
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isLargerTiramisu
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.openUrl
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.showRationaleDialog
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.toast
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible
import java.util.concurrent.Executors

class ScanNo1Activity : BaseNo1Activity<ActivityScanNo1Binding>() {

    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis
    private var scanTypeNo1 = ScanTypeNo1.SINGLE
    private var listBatched: ArrayList<HistoryModel> = arrayListOf()
    private var currentBatchSize = 0
    private var isFlash = false
    private var mediaPlayer: MediaPlayer? = null

    override fun initView() {
        showViewScan()
        binding.icBack.tap {
            startActivity(Intent(this@ScanNo1Activity, MainNo1Activity::class.java))
            finishAffinity()
        }
        binding.layoutScan.tap {
            showViewScan()
        }
        binding.layoutBatch.tap {
            showViewBatch()
        }
        binding.layoutGallery.tap {
            checkExternalStoragePermission(
                onPermissionGranted = {

                    showViewGallery()
                },
                onShowRationale = {
                    showRationaleDialog(
                        desc = if (isLargerTiramisu()) getString(R.string.this_app_need_read_images_permission_to_enhance_feature) else getString(R.string.this_app_need_external_storage_to_enhance_feature),
                        onGoToSetting = {

                            goToSettings()
                        }
                    )
                },
                onRequestPermission = {

                }
            )
        }
        startScanner { barcodes ->
            barcodes.forEach { barcode ->
                if (SharePrefHelper(this).getBoolean(IS_VIBRATE)) {
                    VibrateHelperNo1(this).startVibrate()
                }
                if (SharePrefHelper(this).getBoolean(IS_SOUND)) {
                    mediaPlayer = MediaPlayer.create(this@ScanNo1Activity, R.raw.scan_beep)
                    mediaPlayer?.start()
                }
                when (barcode.valueType) {
                    Barcode.TYPE_URL -> {
                        val url = barcode.url?.url.toString()
                        val bundle = Bundle().apply {
                            putString(Constants.ScreenKey.CONTENT_URL, barcode.url?.url)
                        }
                        if (url.contains(FACEBOOK_SIGN)) {
                            if (scanTypeNo1 == ScanTypeNo1.SINGLE) {
                                goToScanResult(bundle, Type.FACEBOOK, TypeUI.SINGLE)
                                if (SharePrefHelper(this).getBoolean(IS_OPEN_URL)) {
                                    openUrl(url)
                                }
                            } else {
                                setDataOnBatch(url, Type.FACEBOOK)
                                saveHistory(Type.FACEBOOK, url, TypeUI.SINGLE)
                            }
                        } else if (url.contains(INSTAGRAM_SIGN)) {
                            if (scanTypeNo1 == ScanTypeNo1.SINGLE) {
                                goToScanResult(bundle, Type.INSTAGRAM, TypeUI.SINGLE)
                                if (SharePrefHelper(this).getBoolean(IS_OPEN_URL)) {
                                    openUrl(url)
                                }
                            } else {
                                setDataOnBatch(url, Type.INSTAGRAM)
                                saveHistory(Type.INSTAGRAM, url, TypeUI.SINGLE)
                            }
                        } else if (url.contains(YOUTUBE_SIGN)) {
                            if (scanTypeNo1 == ScanTypeNo1.SINGLE) {
                                goToScanResult(bundle, Type.YOUTUBE, TypeUI.SINGLE)
                                if (SharePrefHelper(this).getBoolean(IS_OPEN_URL)) {
                                    openUrl(url)
                                }
                            } else {
                                setDataOnBatch(url, Type.YOUTUBE)
                                saveHistory(Type.YOUTUBE, url, TypeUI.SINGLE)
                            }
                        } else if (url.contains(TWITTER_SIGN)) {
                            if (scanTypeNo1 == ScanTypeNo1.SINGLE) {
                                goToScanResult(bundle, Type.TWITTER, TypeUI.SINGLE)
                                if (SharePrefHelper(this).getBoolean(IS_OPEN_URL)) {
                                    openUrl(url)
                                }
                            } else {
                                setDataOnBatch(url, Type.TWITTER)
                                saveHistory(Type.TWITTER, url, TypeUI.SINGLE)
                            }
                        } else if (url.contains(WHATSAPP_SIGN)) {
                            if (scanTypeNo1 == ScanTypeNo1.SINGLE) {
                                goToScanResult(bundle, Type.WHATSAPP, TypeUI.SINGLE)
                                if (SharePrefHelper(this).getBoolean(IS_OPEN_URL)) {
                                    openUrl(url)
                                }
                            } else {
                                setDataOnBatch(url, Type.WHATSAPP)
                                saveHistory(Type.WHATSAPP, url, TypeUI.SINGLE)
                            }
                        } else {
                            if (scanTypeNo1 == ScanTypeNo1.SINGLE) {
                                goToScanResult(bundle, Type.URL, TypeUI.SINGLE)
                                if (SharePrefHelper(this).getBoolean(IS_OPEN_URL)) {

                                    openUrl(url)
                                }
                            } else {
                                setDataOnBatch(url, Type.URL)
                                saveHistory(Type.URL, url, TypeUI.SINGLE)
                            }
                        }
                    }

                    Barcode.TYPE_TEXT -> {
                        checkScanType(
                            onScanSingle = {
                                val listBarcode1D = mutableListOf(
                                    Barcode.FORMAT_UPC_A, Barcode.FORMAT_UPC_E, Barcode.FORMAT_EAN_8, Barcode.FORMAT_EAN_13,
                                    Barcode.FORMAT_CODE_39, Barcode.FORMAT_CODE_128, Barcode.FORMAT_ITF, Barcode.FORMAT_CODABAR
                                )
                                val format = barcode.format
                                val bundle = Bundle().apply {
                                    putString(CONTENT_TEXT, barcode.rawValue)
                                }
                                if (listBarcode1D.contains(format)) {
                                    goToScanResult(bundle, Type.BARCODE, TypeUI.BARCODE)
                                } else {
                                    goToScanResult(bundle, Type.TEXT, TypeUI.SINGLE)
                                }
                            },
                            onScanBatch = {
                                setDataOnBatch(barcode.rawValue.toString(), Type.TEXT)
                                saveHistory(Type.TEXT, barcode.rawValue.toString(), TypeUI.SINGLE)
                            }
                        )
                    }

                    Barcode.TYPE_WIFI -> {
                        checkScanType(
                            onScanSingle = {
                                val bundle = Bundle().apply {
                                    putString(CONTENT_WIFI, barcode.rawValue)
                                }
                                goToScanResult(bundle, Type.WIFI, TypeUI.MULTIPLE)
                            },
                            onScanBatch = {
                                setDataOnBatch(barcode.rawValue.toString(), Type.WIFI)
                                saveHistory(Type.WIFI, barcode.rawValue.toString(), TypeUI.MULTIPLE)
                            }
                        )
                    }

                    Barcode.TYPE_EMAIL -> {
                        checkScanType(
                            onScanSingle = {
                                val bundle = Bundle().apply {
                                    putString(CONTENT_EMAIL, barcode.rawValue)
                                }
                                goToScanResult(bundle, Type.EMAIL, TypeUI.MULTIPLE)
                            },
                            onScanBatch = {
                                setDataOnBatch(barcode.rawValue.toString(), Type.EMAIL)
                                saveHistory(Type.EMAIL, barcode.rawValue.toString(), TypeUI.MULTIPLE)
                            }
                        )
                    }

                    Barcode.TYPE_SMS -> {
                        checkScanType(
                            onScanSingle = {
                                val bundle = Bundle().apply {
                                    putString(CONTENT_SMS, barcode.rawValue)
                                }
                                goToScanResult(bundle, Type.SMS, TypeUI.MULTIPLE)
                            },
                            onScanBatch = {
                                setDataOnBatch(barcode.rawValue.toString(), Type.SMS)
                                saveHistory(Type.SMS, barcode.rawValue.toString(), TypeUI.MULTIPLE)
                            }
                        )
                    }

                    Barcode.TYPE_PHONE -> {
                        checkScanType(
                            onScanSingle = {
                                val bundle = Bundle().apply {
                                    putString(CONTENT_PHONE, barcode.rawValue)
                                }
                                goToScanResult(bundle, Type.PHONE, TypeUI.SINGLE)
                            },
                            onScanBatch = {
                                setDataOnBatch(barcode.rawValue.toString(), Type.PHONE)
                                saveHistory(Type.PHONE, barcode.rawValue.toString(), TypeUI.SINGLE)
                            }
                        )
                    }

                    Barcode.TYPE_GEO -> {
                        checkScanType(
                            onScanSingle = {
                                val bundle = Bundle().apply {
                                    putString(CONTENT_LOCATION, barcode.rawValue)
                                }
                                goToScanResult(bundle, Type.LOCATION, TypeUI.MULTIPLE)
                            },
                            onScanBatch = {
                                setDataOnBatch(barcode.rawValue.toString(), Type.LOCATION)
                                saveHistory(Type.LOCATION, barcode.rawValue.toString(), TypeUI.MULTIPLE)
                            }
                        )
                    }

                    Barcode.TYPE_CONTACT_INFO -> {
                        checkScanType(
                            onScanSingle = {
                                val bundle = Bundle().apply {
                                    putString(CONTENT_CONTACT, barcode.rawValue)
                                }
                                goToScanResult(bundle, Type.CONTACT, TypeUI.MULTIPLE)
                            },
                            onScanBatch = {
                                setDataOnBatch(barcode.rawValue.toString(), Type.CONTACT)
                                saveHistory(Type.CONTACT, barcode.rawValue.toString(), TypeUI.MULTIPLE)
                            }
                        )
                    }

                    Barcode.TYPE_PRODUCT -> {
                        checkScanType(
                            onScanSingle = {
                                val bundle = Bundle().apply {
                                    putString(CONTENT_TEXT, barcode.rawValue)
                                }
                                goToScanResult(bundle, Type.BARCODE, TypeUI.BARCODE)
                            },
                            onScanBatch = {
                                setDataOnBatch(barcode.rawValue.toString(), Type.BARCODE)
                                saveHistory(Type.BARCODE, barcode.rawValue.toString(), TypeUI.BARCODE)
                            }
                        )
                    }

                    Barcode.TYPE_UNKNOWN -> {
                        toast(getString(R.string.this_code_is_not_supported))
                    }
                }
            }
        }
        binding.icNextAfterBatch.tap {
            val intent = Intent(this, ScanBatchResultNo1Activity::class.java)
            val bundle = Bundle()
            bundle.putInt("size_batch", listBatched.size)
            for (i in 0 until listBatched.size) {
                bundle.putSerializable("item_batch_$i", listBatched[i])
            }
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun setDataOnBatch(content: String, type: Type) {
        currentBatchSize++
        binding.layoutBatchOn.visible()
        binding.tvCountBatch.text = currentBatchSize.toString()
        binding.tvCurrentBatch.text = content
        listBatched.add(HistoryModel(-1, type.toString(), "", "", content, "", "", ""))
    }

    override fun initData() {
        cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            processCameraProvider = cameraProviderFuture.get()
            bindCameraPreview()
            bindInputAnalyser()
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onBackPressCustom() {
        startActivity(Intent(this@ScanNo1Activity, MainNo1Activity::class.java))
        finishAffinity()
    }

    private fun bindInputAnalyser() {
        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
        )
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(binding.cameraPreview.display?.rotation ?: Surface.ROTATION_0)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(Size(1920, 1080))
            .build()
        val cameraExecutor = Executors.newSingleThreadExecutor()
        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            processImageProxy(barcodeScanner, imageProxy)
        }
        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
    }

    private fun bindCameraPreview() {
        cameraPreview = Preview.Builder()
            .setTargetRotation(binding.cameraPreview.display?.rotation ?: Surface.ROTATION_0)
            .build()
        cameraPreview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        val camera = processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview)
        val cameraControl = camera.cameraControl
        binding.icFlash.tap {
            isFlash = !isFlash
            cameraControl.enableTorch(isFlash)
            if (isFlash) {
                binding.icFlash.setImageResource(R.drawable.ic_flash_on)
            } else {
                binding.icFlash.setImageResource(R.drawable.ic_flash_off)
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        val mediaImage = imageProxy.image ?: return
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcode ->
                val previewWidth = binding.cameraPreview.width
                val previewHeight = binding.cameraPreview.height
                val imageWidth = inputImage.width
                val imageHeight = inputImage.height
                val scanRect = binding.overlayView.getScanRect()
                val scanRectInImage = convertPreviewToImageRect(scanRect, imageWidth, imageHeight, previewWidth, previewHeight)
                for (code in barcode) {
                    val boundingBox = code.boundingBox
                    if (boundingBox != null && scanRectInImage.intersect(boundingBox)) {
                        if (barcode.isNotEmpty()) {
                            when (scanTypeNo1) {
                                ScanTypeNo1.SINGLE -> {
                                    onScan?.invoke(barcode)
                                    onScan = null
                                }

                                ScanTypeNo1.BATCH -> {
                                    if (!listBatched.contains(listBatched.find { it.contentRawValue == code.rawValue })) {
                                        onScan?.invoke(barcode)
                                    }
                                }
                            }
                        } else {
                            toast(getString(R.string.cant_detect))
                        }
                    }
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }

    }

    override fun getViewBinding(): ActivityScanNo1Binding {
        return ActivityScanNo1Binding.inflate(layoutInflater)
    }

    private fun convertPreviewToImageRect(previewRect: Rect, imageWidth: Int, imageHeight: Int, previewWidth: Int, previewHeight: Int): Rect {
        val scaleX = imageWidth.toFloat() / previewWidth.toFloat()
        val scaleY = imageHeight.toFloat() / previewHeight.toFloat()
        return Rect(
            (previewRect.left * scaleX).toInt(),
            (previewRect.top * scaleY).toInt(),
            (previewRect.right * scaleX).toInt(),
            (previewRect.bottom * scaleY).toInt()
        )
    }

    private fun goToScanResult(bundle: Bundle, type: Type, typeUI: TypeUI) {
        bundle.apply {
            putString(Constants.ScreenKey.CODE_TYPE, type.toString())
            putString(Constants.ScreenKey.UI_CODE_TYPE, typeUI.toString())
            putString(Constants.ScreenKey.KEY_SCREEN_TO_RESULT, "scan")
            putString(Constants.ScreenKey.TIME_HOUR, getCurrentHHMM())
            putString(Constants.ScreenKey.TIME_DATE, getCurrentDate())
        }
        val intent = Intent(this, ScanResultNo1Activity::class.java).apply {
            putExtras(bundle)
        }
        startActivity(intent)
        finish()
    }

    private fun checkScanType(onScanSingle: () -> Unit, onScanBatch: () -> Unit) {
        when (scanTypeNo1) {
            ScanTypeNo1.SINGLE -> onScanSingle.invoke()
            ScanTypeNo1.BATCH -> onScanBatch.invoke()
        }
    }

    private fun showViewScan() {
        scanTypeNo1 = ScanTypeNo1.SINGLE
        binding.layoutBatchOn.gone()
        setSelectedAction(binding.layoutScan, binding.ivQr, binding.tvQr)
        setUnSelectedAction(binding.layoutBatch, binding.ivBatch, binding.tvBatch)
        setUnSelectedAction(binding.layoutGallery, binding.ivGallery, binding.tvGallery)
    }

    private fun showViewBatch() {
        scanTypeNo1 = ScanTypeNo1.BATCH
        setSelectedAction(binding.layoutBatch, binding.ivBatch, binding.tvBatch)
        setUnSelectedAction(binding.layoutScan, binding.ivQr, binding.tvQr)
        setUnSelectedAction(binding.layoutGallery, binding.ivGallery, binding.tvGallery)
    }

    private fun showViewGallery() {
        AppOpenManager.getInstance().disableAppResumeWithActivity(javaClass)
        binding.layoutBatchOn.gone()
        setSelectedAction(binding.layoutGallery, binding.ivGallery, binding.tvGallery)
        setUnSelectedAction(binding.layoutBatch, binding.ivBatch, binding.tvBatch)
        setUnSelectedAction(binding.layoutScan, binding.ivQr, binding.tvQr)
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        galleryLaunch.launch(intent)
    }

    private val galleryLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.data != null) {
                try {
                    val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(contentResolver, it.data!!.data)
                    } else {
                        val source = ImageDecoder.createSource(contentResolver, it.data?.data!!)
                        ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true)
                    }
                    detectCodeInImageWithMLKit(bitmap) { barcodes ->
                        if (barcodes.isNotEmpty()) {
                            for (barcode in barcodes) {
                                when (barcode.valueType) {
                                    Barcode.TYPE_URL -> {
                                        val url = barcode.url?.url.toString()
                                        val bundle = Bundle().apply {
                                            putString(
                                                Constants.ScreenKey.CONTENT_URL,
                                                barcode.url?.url
                                            )
                                        }
                                        if (url.contains(FACEBOOK_SIGN)) {
                                            goToScanResult(bundle, Type.FACEBOOK, TypeUI.SINGLE)
                                        } else if (url.contains(INSTAGRAM_SIGN)) {
                                            goToScanResult(bundle, Type.INSTAGRAM, TypeUI.SINGLE)
                                        } else if (url.contains(YOUTUBE_SIGN)) {
                                            goToScanResult(bundle, Type.YOUTUBE, TypeUI.SINGLE)
                                        } else if (url.contains(TWITTER_SIGN)) {
                                            goToScanResult(bundle, Type.TWITTER, TypeUI.SINGLE)
                                        } else if (url.contains(WHATSAPP_SIGN)) {
                                            goToScanResult(bundle, Type.WHATSAPP, TypeUI.SINGLE)
                                        } else {
                                            goToScanResult(bundle, Type.URL, TypeUI.SINGLE)
                                        }
                                    }

                                    Barcode.TYPE_TEXT -> {
                                        val listBarcode1D = mutableListOf(
                                            Barcode.FORMAT_UPC_A,
                                            Barcode.FORMAT_UPC_E,
                                            Barcode.FORMAT_EAN_8,
                                            Barcode.FORMAT_EAN_13,
                                            Barcode.FORMAT_CODE_39,
                                            Barcode.FORMAT_CODE_128,
                                            Barcode.FORMAT_ITF,
                                            Barcode.FORMAT_CODABAR
                                        )
                                        val format = barcode.format
                                        val bundle = Bundle().apply {
                                            putString(CONTENT_TEXT, barcode.rawValue)
                                        }
                                        if (listBarcode1D.contains(format)) {
                                            goToScanResult(bundle, Type.BARCODE, TypeUI.BARCODE)
                                        } else {
                                            goToScanResult(bundle, Type.TEXT, TypeUI.SINGLE)
                                        }
                                    }

                                    Barcode.TYPE_WIFI -> {
                                        val bundle = Bundle().apply {
                                            putString(CONTENT_WIFI, barcode.rawValue)
                                        }
                                        goToScanResult(bundle, Type.WIFI, TypeUI.MULTIPLE)
                                    }

                                    Barcode.TYPE_EMAIL -> {
                                        val bundle = Bundle().apply {
                                            putString(CONTENT_EMAIL, barcode.rawValue)
                                        }
                                        goToScanResult(bundle, Type.EMAIL, TypeUI.MULTIPLE)
                                    }

                                    Barcode.TYPE_SMS -> {
                                        val bundle = Bundle().apply {
                                            putString(CONTENT_SMS, barcode.rawValue)
                                        }
                                        goToScanResult(bundle, Type.SMS, TypeUI.MULTIPLE)
                                    }

                                    Barcode.TYPE_PHONE -> {
                                        val bundle = Bundle().apply {
                                            putString(CONTENT_PHONE, barcode.rawValue)
                                        }
                                        goToScanResult(bundle, Type.PHONE, TypeUI.SINGLE)
                                    }

                                    Barcode.TYPE_GEO -> {
                                        val bundle = Bundle().apply {
                                            putString(CONTENT_LOCATION, barcode.rawValue)
                                        }
                                        goToScanResult(bundle, Type.LOCATION, TypeUI.MULTIPLE)
                                    }

                                    Barcode.TYPE_CONTACT_INFO -> {
                                        val bundle = Bundle().apply {
                                            putString(CONTENT_CONTACT, barcode.rawValue)
                                        }
                                        goToScanResult(bundle, Type.CONTACT, TypeUI.MULTIPLE)
                                    }

                                    Barcode.TYPE_PRODUCT -> {
                                        val bundle = Bundle().apply {
                                            putString(CONTENT_TEXT, barcode.rawValue)
                                        }
                                        goToScanResult(bundle, Type.BARCODE, TypeUI.SINGLE)
                                    }

                                    Barcode.TYPE_UNKNOWN -> {
                                        toast(getString(R.string.this_code_is_not_supported))
                                        return@detectCodeInImageWithMLKit
                                    }
                                }
                            }
                        } else {
                            toast(getString(R.string.cant_detect_code_in_image))
                        }
                    }
                } catch (e: Exception) {
                    Log.i("gallery", e.toString())
                    toast(getString(R.string.cant_find_code_in_image))
                    return@registerForActivityResult
                }
            }
        }
    }

    private fun saveHistory(type: Type, content: String, uiType: TypeUI) {
        AppDatabaseNo1.getInstance(this).appDao().insertHistoryModel(
            HistoryModel(
                id = 0,
                codeType = type.toString(),
                uiType = uiType.toString(),
                historyType = HistoryType.SCANNED.toString(),
                contentRawValue = content,
                timeHour = getCurrentHHMM(),
                timeDate = getCurrentDate(),
                timeDone = System.currentTimeMillis().toString()
            )
        )
    }


    private fun setSelectedAction(
        ll: LinearLayout,
        img: ImageView,
        tv: TextView
    ) {
        ll.setBackgroundResource(R.drawable.bg_white)
        img.setColorFilter(ContextCompat.getColor(this, R.color.black_app))
        tv.setTextColor(resources.getColor(R.color.black_app, null))
    }

    private fun setUnSelectedAction(
        ll: LinearLayout,
        img: ImageView,
        tv: TextView
    ) {
        ll.setBackgroundResource(R.drawable.bg_action_scan_unselect)
        img.setColorFilter(ContextCompat.getColor(this, R.color.black_app))
        tv.setTextColor(resources.getColor(R.color.black_app, null))
    }

    override fun onResume() {
        super.onResume()
        showViewScan()
    }

    companion object {
        private var onScan: ((barcodes: List<Barcode>) -> Unit)? = null
        fun startScanner(onScan: (barcodes: List<Barcode>) -> Unit) {
            this.onScan = onScan
        }
    }

    override fun onStop() {
        super.onStop()
        binding.icFlash.setImageResource(R.drawable.ic_flash_off)
        isFlash = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

}
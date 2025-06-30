package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityTemplateNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutTabTemplateNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.result_scan.ScanResultNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.result_scan.ScanResultNo1Activity.Companion.myTemplateBitmap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.background.BackgroundAdapterNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.background.BackgroundModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.color.ColorAdapterNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.color.ColorModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.logo.LogoAdapterNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.logo.LogoModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.template_sub.TemplateAdapterNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.template_sub.TemplateModelNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.IntentKey.BACKGROUND
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.IntentKey.CHANGE_TEMPLATE_DONE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.IntentKey.COLOR
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.IntentKey.LOGO
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.IntentKey.TEMPLATE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.checkExternalStoragePermission
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.getRoundedCornerBitmap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.goToSettings
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.invisible
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isLargerTiramisu
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.scaleBitmap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.showRationaleDialog
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.toast
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TemplateNo1Activity : BaseNo1Activity<ActivityTemplateNo1Binding>() {
    private lateinit var tabBinding: LayoutTabTemplateNo1Binding
    private var listBackground = mutableListOf<BackgroundModelNo1>()
    private var listColor = mutableListOf<ColorModelNo1>()
    private var listLogo = mutableListOf<LogoModelNo1>()
    private var listTemplate = mutableListOf<TemplateModelNo1>()
    private var backgroundAdapterNo1: BackgroundAdapterNo1? = null
    private var colorAdapterNo1: ColorAdapterNo1? = null
    private var logoAdapterNo1: LogoAdapterNo1? = null
    private var templateAdapterNo1: TemplateAdapterNo1? = null
    private var backgroundModelNo1: BackgroundModelNo1? = null
    private var colorModelNo1: ColorModelNo1? = null
    private var logoModelNo1: LogoModelNo1? = null
    private var templateModelNo1: TemplateModelNo1? = null

    override fun getViewBinding(): ActivityTemplateNo1Binding = ActivityTemplateNo1Binding.inflate(layoutInflater)


    override fun initView() {
        tabBinding = LayoutTabTemplateNo1Binding.bind(binding.includeTabTemplate.root)
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action.equals(Intent.ACTION_MAIN)
        ) {
            finish()
            return
        }
        tabBinding.llBackground.setOnClickListener {
            changeBottomNavStatus(tabBinding.llBackground)
            setUpBackground()
        }
        tabBinding.llColor.setOnClickListener {
            changeBottomNavStatus(tabBinding.llColor)
            setUpColor()
        }
        tabBinding.llLogo.setOnClickListener {
            changeBottomNavStatus(tabBinding.llLogo)
            setUpLogo()
        }
        tabBinding.llTemplate.setOnClickListener {
            changeBottomNavStatus(tabBinding.llTemplate)
            setUpTemplate()
        }
        binding.icBack.tap {
            finish()
        }
        binding.icDone.tap {
            val intent = Intent(CHANGE_TEMPLATE_DONE)
            intent.putExtra(BACKGROUND, backgroundModelNo1)
            intent.putExtra(COLOR, colorModelNo1)
            intent.putExtra(LOGO, logoModelNo1)
            intent.putExtra(TEMPLATE, templateModelNo1)
            LocalBroadcastManager.getInstance(this@TemplateNo1Activity).sendBroadcast(intent)
            finish()
        }
        binding.imgQr.setImageBitmap(ScanResultNo1Activity.myQRBitmap)
    }

    override fun initData() {
        backgroundModelNo1 = intent.getSerializableExtra(BACKGROUND) as BackgroundModelNo1?
        colorModelNo1 = intent.getSerializableExtra(COLOR) as ColorModelNo1?
        logoModelNo1 = intent.getSerializableExtra(LOGO) as LogoModelNo1?
        templateModelNo1 = intent.getSerializableExtra(TEMPLATE) as TemplateModelNo1?

        if (backgroundModelNo1 != null && backgroundModelNo1?.backgroundImage != null) {
            binding.rlQr.setBackgroundResource(backgroundModelNo1?.backgroundImage!!)
        } else {
            binding.rlQr.setBackgroundResource(R.drawable.ic_background_white_template)
        }
        backgroundModelNo1?.backgroundImage?.let { it1 ->
            binding.rlQr.setBackgroundResource(it1)
        }
        colorModelNo1?.colorImage?.let {
            binding.imgQr.setColorFilter(Color.parseColor(it), PorterDuff.Mode.SRC_IN)
        }
        logoModelNo1?.logoImage?.let { it1 -> binding.imgLogo.setImageResource(it1) }
        templateModelNo1?.templateImage?.let { it1 -> binding.ctQr.setBackgroundResource(it1) }

        myTemplateBitmap?.let {
            binding.ctQr.background = BitmapDrawable(resources, it)
        }

        addListBackground()
        addListColor()
        addListLogo()
        addListTemplate()
        setUpBackground()
    }

    override fun onBackPressCustom() {
        finish()
    }

    private fun setUpBackground() {
        backgroundAdapterNo1 = BackgroundAdapterNo1(this@TemplateNo1Activity, listBackground,
            onClickItem = {
                backgroundModelNo1 = it
                it.backgroundImage?.let { it1 -> binding.rlQr.setBackgroundResource(it1) }
            },
            onClickNone = {
                backgroundModelNo1 = null
                binding.rlQr.setBackgroundColor(Color.WHITE)
            }
        )
        val layoutManager = GridLayoutManager(this@TemplateNo1Activity, 6)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = backgroundAdapterNo1
        try {
            backgroundModelNo1?.let { backgroundAdapterNo1?.selectItem(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUpColor() {
        colorAdapterNo1 = ColorAdapterNo1(this@TemplateNo1Activity, listColor,
            onClickItem = {
                colorModelNo1 = it
                binding.imgQr.setColorFilter(Color.parseColor(it.colorImage), PorterDuff.Mode.SRC_IN)
            },
            onClickNone = {
                binding.imgQr.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN)
                colorModelNo1 = null
            }
        )
        val layoutManager = GridLayoutManager(this@TemplateNo1Activity, 6)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = colorAdapterNo1
        colorModelNo1?.let { colorAdapterNo1?.selectItem(it) }
    }

    private fun setUpLogo() {
        logoAdapterNo1 = LogoAdapterNo1(this@TemplateNo1Activity, listLogo,
            onClickItem = {
                logoModelNo1 = it
                binding.imgLogo.visible()
                it.logoImage?.let { it1 -> binding.imgLogo.setImageResource(it1) }
            },
            onClickNone = {
                logoModelNo1 = null
                binding.imgLogo.invisible()
            }
        )
        val layoutManager = GridLayoutManager(this@TemplateNo1Activity, 5)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = logoAdapterNo1
        logoModelNo1?.let { logoAdapterNo1?.selectItem(it) }
    }

    private fun setUpTemplate() {
        templateAdapterNo1 = TemplateAdapterNo1(this@TemplateNo1Activity, listTemplate,
            onClickItem = {
                myTemplateBitmap = null
                templateModelNo1 = it
                it.templateImage?.let { it1 -> binding.ctQr.setBackgroundResource(it1) }
            },
            onClickNone = {
                myTemplateBitmap = null
                templateModelNo1 = null
                binding.ctQr.setBackgroundColor(Color.WHITE)
            },
            onClickPickImage = {
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
        )
        val layoutManager = GridLayoutManager(this@TemplateNo1Activity, 4)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = templateAdapterNo1
        templateModelNo1?.let { templateAdapterNo1?.selectItem(it) }
    }

    private fun showViewGallery() {
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
                    binding.progressBar.visible()
                    var bitmap: Bitmap? = null
                    lifecycleScope.launch(Dispatchers.IO) {
                        bitmap = if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(contentResolver, it.data!!.data)
                        } else {
                            val source = ImageDecoder.createSource(contentResolver, it.data?.data!!)
                            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true)
                        }
                        bitmap?.let {
                            bitmap = scaleBitmap(it, 800, 600)
                        }
                        bitmap?.let {
                            bitmap = getRoundedCornerBitmap(it, 10f)
                            myTemplateBitmap = bitmap
                        }
                    }.invokeOnCompletion {
                        MainScope().launch {
                            myTemplateBitmap?.let {
                                binding.ctQr.background = BitmapDrawable(resources, it)
                            }
                            binding.progressBar.gone()
                            templateAdapterNo1?.selectNone()
                        }
                    }
                } catch (e: Exception) {
                    Log.i("gallery", e.toString())
                    toast(getString(R.string.cant_find_code_in_image))
                    binding.progressBar.gone()
                    return@registerForActivityResult
                }
            }
        }
    }

    private fun addListBackground() {
        lifecycleScope.launch(Dispatchers.IO) {
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background1))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background2))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background3))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background4))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background5))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background6))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background7))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background8))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background9))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background10))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background11))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background12))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background13))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background14))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background15))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background16))
            listBackground.add(BackgroundModelNo1(R.drawable.ic_background17))
            listBackground.shuffle()
            listBackground.add(0, BackgroundModelNo1(null))
        }
    }

    private fun addListColor() {
        lifecycleScope.launch(Dispatchers.IO) {
            listColor.add(ColorModelNo1("#F37272"))
            listColor.add(ColorModelNo1("#F8DB76"))
            listColor.add(ColorModelNo1("#FFB3DA"))
            listColor.add(ColorModelNo1("#82BFB1"))
            listColor.add(ColorModelNo1("#FF8255"))
            listColor.add(ColorModelNo1("#C2A5F9"))
            listColor.add(ColorModelNo1("#78CEEC"))
            listColor.add(ColorModelNo1("#9DC769"))
            listColor.add(ColorModelNo1("#8D78E4"))
            listColor.add(ColorModelNo1("#F9C273"))
            listColor.add(ColorModelNo1("#8094D0"))
            listColor.add(ColorModelNo1("#14B8A6"))
            listColor.add(ColorModelNo1("#FDA4A9"))
            listColor.add(ColorModelNo1("#4B933F"))
            listColor.add(ColorModelNo1("#FFAB91"))
            listColor.add(ColorModelNo1("#000000"))
            listColor.add(ColorModelNo1("#A8A8A8"))
            listColor.shuffle()
            listColor.add(0, ColorModelNo1(null))
        }
    }

    private fun addListLogo() {
        lifecycleScope.launch(Dispatchers.IO) {
            /*listLogo.add(LogoModelNo1(R.drawable.text_frame_83))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_84))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_85))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_86))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_87))*/
            listLogo.add(LogoModelNo1(R.drawable.text_frame_88))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_89))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_90))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_92))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_93))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_94))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_95))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_96))
            listLogo.add(LogoModelNo1(R.drawable.text_frame_97))
            listLogo.shuffle()
            listLogo.add(0, LogoModelNo1(null))
        }
    }

    private fun addListTemplate() {
        lifecycleScope.launch(Dispatchers.IO) {
            listTemplate.add(TemplateModelNo1(R.drawable.img_template1))
            listTemplate.add(TemplateModelNo1(R.drawable.img_template2))
            listTemplate.add(TemplateModelNo1(R.drawable.img_template3))
            listTemplate.add(TemplateModelNo1(R.drawable.img_template5))
            listTemplate.add(TemplateModelNo1(R.drawable.img_template6))
            listTemplate.add(TemplateModelNo1(R.drawable.img_template7))
            listTemplate.add(TemplateModelNo1(R.drawable.img_template8))
            listTemplate.add(TemplateModelNo1(R.drawable.img_template9))
            listTemplate.add(TemplateModelNo1(R.drawable.img_template10))
            listTemplate.shuffle()
            listTemplate.add(0, TemplateModelNo1(null))
            listTemplate.add(1, TemplateModelNo1(null))
        }
    }

    private fun changeBottomNavStatus(view: View) {
        when (view) {
            tabBinding.llBackground -> {
                //tabBinding.llBackgroundImage.setBackgroundResource(R.drawable.bg_bottom_nav_select)
                tabBinding.llColorImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.llLogoImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.llTemplateImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.imgBackground.setImageResource(R.drawable.ic_background_select)
                tabBinding.imgColor.setImageResource(R.drawable.ic_color_unselect)
                tabBinding.imgLogo.setImageResource(R.drawable.ic_logo_unselect)
                tabBinding.imgTemplate.setImageResource(R.drawable.ic_template_unselect)
                tabBinding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.color_6B32F0))
                tabBinding.tvColor.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvLogo.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvTemplate.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
            }

            tabBinding.llColor -> {
                tabBinding.llBackgroundImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                //tabBinding.llColorImage.setBackgroundResource(R.drawable.bg_bottom_nav_select)
                tabBinding.llLogoImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.llTemplateImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.imgBackground.setImageResource(R.drawable.ic_background_unselect)
                tabBinding.imgColor.setImageResource(R.drawable.ic_color_select)
                tabBinding.imgLogo.setImageResource(R.drawable.ic_logo_unselect)
                tabBinding.imgTemplate.setImageResource(R.drawable.ic_template_unselect)
                tabBinding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvColor.setTextColor(ContextCompat.getColor(this, R.color.color_6B32F0))
                tabBinding.tvLogo.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvTemplate.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
            }

            tabBinding.llLogo -> {
                tabBinding.llBackgroundImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.llColorImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                //tabBinding.llLogoImage.setBackgroundResource(R.drawable.bg_bottom_nav_select)
                tabBinding.llTemplateImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.imgBackground.setImageResource(R.drawable.ic_background_unselect)
                tabBinding.imgColor.setImageResource(R.drawable.ic_color_unselect)
                tabBinding.imgLogo.setImageResource(R.drawable.ic_logo_select)
                tabBinding.imgTemplate.setImageResource(R.drawable.ic_template_unselect)
                tabBinding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvColor.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvLogo.setTextColor(ContextCompat.getColor(this, R.color.color_6B32F0))
                tabBinding.tvTemplate.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
            }

            tabBinding.llTemplate -> {
                tabBinding.llBackgroundImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.llColorImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                tabBinding.llLogoImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                //tabBinding.llTemplateImage.setBackgroundResource(R.drawable.bg_bottom_nav_select)
                tabBinding.imgBackground.setImageResource(R.drawable.ic_background_unselect)
                tabBinding.imgColor.setImageResource(R.drawable.ic_color_unselect)
                tabBinding.imgLogo.setImageResource(R.drawable.ic_logo_unselect)
                tabBinding.imgTemplate.setImageResource(R.drawable.ic_template_select)
                tabBinding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvColor.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvLogo.setTextColor(ContextCompat.getColor(this, R.color.color_8E8E93))
                tabBinding.tvTemplate.setTextColor(ContextCompat.getColor(this, R.color.color_6B32F0))
            }
        }
    }
}


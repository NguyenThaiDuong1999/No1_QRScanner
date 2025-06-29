package com.scanqr.qrscanner.qrgenerator.feature.template

import android.annotation.SuppressLint
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
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityTemplateBinding
import com.scanqr.qrscanner.qrgenerator.databinding.LayoutTabTemplateBinding
import com.scanqr.qrscanner.qrgenerator.feature.result_scan.ScanResultActivity
import com.scanqr.qrscanner.qrgenerator.feature.result_scan.ScanResultActivity.Companion.myTemplateBitmap
import com.scanqr.qrscanner.qrgenerator.feature.template.background.BackgroundAdapter
import com.scanqr.qrscanner.qrgenerator.feature.template.background.BackgroundModel
import com.scanqr.qrscanner.qrgenerator.feature.template.color.ColorAdapter
import com.scanqr.qrscanner.qrgenerator.feature.template.color.ColorModel
import com.scanqr.qrscanner.qrgenerator.feature.template.logo.LogoAdapter
import com.scanqr.qrscanner.qrgenerator.feature.template.logo.LogoModel
import com.scanqr.qrscanner.qrgenerator.feature.template.template_sub.TemplateAdapter
import com.scanqr.qrscanner.qrgenerator.feature.template.template_sub.TemplateModel
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.BACKGROUND
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.CHANGE_TEMPLATE_DONE
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.COLOR
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.LOGO
import com.scanqr.qrscanner.qrgenerator.utils.Constants.IntentKey.TEMPLATE
import com.scanqr.qrscanner.qrgenerator.utils.checkExternalStoragePermission
import com.scanqr.qrscanner.qrgenerator.utils.getRoundedCornerBitmap
import com.scanqr.qrscanner.qrgenerator.utils.goToSettings
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.invisible
import com.scanqr.qrscanner.qrgenerator.utils.isLargerTiramisu
import com.scanqr.qrscanner.qrgenerator.utils.scaleBitmap
import com.scanqr.qrscanner.qrgenerator.utils.showRationaleDialog
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.toast
import com.scanqr.qrscanner.qrgenerator.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TemplateActivity : BaseActivity<ActivityTemplateBinding>() {
    private lateinit var tabBinding: LayoutTabTemplateBinding
    private var listBackground = mutableListOf<BackgroundModel>()
    private var listColor = mutableListOf<ColorModel>()
    private var listLogo = mutableListOf<LogoModel>()
    private var listTemplate = mutableListOf<TemplateModel>()
    private var backgroundAdapter: BackgroundAdapter? = null
    private var colorAdapter: ColorAdapter? = null
    private var logoAdapter: LogoAdapter? = null
    private var templateAdapter: TemplateAdapter? = null
    private var backgroundModel: BackgroundModel? = null
    private var colorModel: ColorModel? = null
    private var logoModel: LogoModel? = null
    private var templateModel: TemplateModel? = null

    override fun getViewBinding(): ActivityTemplateBinding = ActivityTemplateBinding.inflate(layoutInflater)


    override fun initView() {
        tabBinding = LayoutTabTemplateBinding.bind(binding.includeTabTemplate.root)
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
            intent.putExtra(BACKGROUND, backgroundModel)
            intent.putExtra(COLOR, colorModel)
            intent.putExtra(LOGO, logoModel)
            intent.putExtra(TEMPLATE, templateModel)
            LocalBroadcastManager.getInstance(this@TemplateActivity).sendBroadcast(intent)
            finish()
        }
        binding.imgQr.setImageBitmap(ScanResultActivity.myQRBitmap)
    }

    override fun initData() {
        backgroundModel = intent.getSerializableExtra(BACKGROUND) as BackgroundModel?
        colorModel = intent.getSerializableExtra(COLOR) as ColorModel?
        logoModel = intent.getSerializableExtra(LOGO) as LogoModel?
        templateModel = intent.getSerializableExtra(TEMPLATE) as TemplateModel?

        if (backgroundModel != null && backgroundModel?.backgroundImage != null) {
            binding.rlQr.setBackgroundResource(backgroundModel?.backgroundImage!!)
        } else {
            binding.rlQr.setBackgroundResource(R.drawable.ic_background_white_template)
        }
        backgroundModel?.backgroundImage?.let { it1 ->
            binding.rlQr.setBackgroundResource(it1)
        }
        colorModel?.colorImage?.let {
            binding.imgQr.setColorFilter(Color.parseColor(it), PorterDuff.Mode.SRC_IN)
        }
        logoModel?.logoImage?.let { it1 -> binding.imgLogo.setImageResource(it1) }
        templateModel?.templateImage?.let { it1 -> binding.ctQr.setBackgroundResource(it1) }

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
        backgroundAdapter = BackgroundAdapter(this@TemplateActivity, listBackground,
            onClickItem = {
                backgroundModel = it
                it.backgroundImage?.let { it1 -> binding.rlQr.setBackgroundResource(it1) }
            },
            onClickNone = {
                backgroundModel = null
                binding.rlQr.setBackgroundColor(Color.WHITE)
            }
        )
        val layoutManager = GridLayoutManager(this@TemplateActivity, 6)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = backgroundAdapter
        try {
            backgroundModel?.let { backgroundAdapter?.selectItem(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUpColor() {
        colorAdapter = ColorAdapter(this@TemplateActivity, listColor,
            onClickItem = {
                colorModel = it
                binding.imgQr.setColorFilter(Color.parseColor(it.colorImage), PorterDuff.Mode.SRC_IN)
            },
            onClickNone = {
                binding.imgQr.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN)
                colorModel = null
            }
        )
        val layoutManager = GridLayoutManager(this@TemplateActivity, 6)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = colorAdapter
        colorModel?.let { colorAdapter?.selectItem(it) }
    }

    private fun setUpLogo() {
        logoAdapter = LogoAdapter(this@TemplateActivity, listLogo,
            onClickItem = {
                logoModel = it
                binding.imgLogo.visible()
                it.logoImage?.let { it1 -> binding.imgLogo.setImageResource(it1) }
            },
            onClickNone = {
                logoModel = null
                binding.imgLogo.invisible()
            }
        )
        val layoutManager = GridLayoutManager(this@TemplateActivity, 5)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = logoAdapter
        logoModel?.let { logoAdapter?.selectItem(it) }
    }

    private fun setUpTemplate() {
        templateAdapter = TemplateAdapter(this@TemplateActivity, listTemplate,
            onClickItem = {
                myTemplateBitmap = null
                templateModel = it
                it.templateImage?.let { it1 -> binding.ctQr.setBackgroundResource(it1) }
            },
            onClickNone = {
                myTemplateBitmap = null
                templateModel = null
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
        val layoutManager = GridLayoutManager(this@TemplateActivity, 4)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = templateAdapter
        templateModel?.let { templateAdapter?.selectItem(it) }
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
                            templateAdapter?.selectNone()
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
            listBackground.add(BackgroundModel(null))
            listBackground.add(BackgroundModel(R.drawable.ic_background1))
            listBackground.add(BackgroundModel(R.drawable.ic_background2))
            listBackground.add(BackgroundModel(R.drawable.ic_background3))
            listBackground.add(BackgroundModel(R.drawable.ic_background4))
            listBackground.add(BackgroundModel(R.drawable.ic_background5))
            listBackground.add(BackgroundModel(R.drawable.ic_background6))
            listBackground.add(BackgroundModel(R.drawable.ic_background7))
            listBackground.add(BackgroundModel(R.drawable.ic_background8))
            listBackground.add(BackgroundModel(R.drawable.ic_background9))
            listBackground.add(BackgroundModel(R.drawable.ic_background10))
            listBackground.add(BackgroundModel(R.drawable.ic_background11))
            listBackground.add(BackgroundModel(R.drawable.ic_background12))
            listBackground.add(BackgroundModel(R.drawable.ic_background13))
            listBackground.add(BackgroundModel(R.drawable.ic_background14))
            listBackground.add(BackgroundModel(R.drawable.ic_background15))
            listBackground.add(BackgroundModel(R.drawable.ic_background16))
            listBackground.add(BackgroundModel(R.drawable.ic_background17))
        }
    }

    private fun addListColor() {
        lifecycleScope.launch(Dispatchers.IO) {
            listColor.add(ColorModel(null))
            listColor.add(ColorModel("#F37272"))
            listColor.add(ColorModel("#F8DB76"))
            listColor.add(ColorModel("#FFB3DA"))
            listColor.add(ColorModel("#82BFB1"))
            listColor.add(ColorModel("#FF8255"))
            listColor.add(ColorModel("#C2A5F9"))
            listColor.add(ColorModel("#78CEEC"))
            listColor.add(ColorModel("#9DC769"))
            listColor.add(ColorModel("#8D78E4"))
            listColor.add(ColorModel("#F9C273"))
            listColor.add(ColorModel("#8094D0"))
            listColor.add(ColorModel("#14B8A6"))
            listColor.add(ColorModel("#FDA4A9"))
            listColor.add(ColorModel("#4B933F"))
            listColor.add(ColorModel("#FFAB91"))
            listColor.add(ColorModel("#000000"))
            listColor.add(ColorModel("#A8A8A8"))
        }
    }

    private fun addListLogo() {
        lifecycleScope.launch(Dispatchers.IO) {
            listLogo.add(LogoModel(null))
            listLogo.add(LogoModel(R.drawable.text_frame_83))
            listLogo.add(LogoModel(R.drawable.text_frame_84))
            listLogo.add(LogoModel(R.drawable.text_frame_85))
            listLogo.add(LogoModel(R.drawable.text_frame_86))
            listLogo.add(LogoModel(R.drawable.text_frame_87))
            listLogo.add(LogoModel(R.drawable.text_frame_88))
            listLogo.add(LogoModel(R.drawable.text_frame_89))
            listLogo.add(LogoModel(R.drawable.text_frame_90))
            listLogo.add(LogoModel(R.drawable.text_frame_92))
            listLogo.add(LogoModel(R.drawable.text_frame_93))
            listLogo.add(LogoModel(R.drawable.text_frame_94))
            listLogo.add(LogoModel(R.drawable.text_frame_95))
            listLogo.add(LogoModel(R.drawable.text_frame_96))
            listLogo.add(LogoModel(R.drawable.text_frame_97))
        }
    }

    private fun addListTemplate() {
        lifecycleScope.launch(Dispatchers.IO) {
            listTemplate.add(TemplateModel(null))
            listTemplate.add(TemplateModel(null))
            listTemplate.add(TemplateModel(R.drawable.img_template1))
            listTemplate.add(TemplateModel(R.drawable.img_template2))
            listTemplate.add(TemplateModel(R.drawable.img_template3))
            listTemplate.add(TemplateModel(R.drawable.img_template4))
            listTemplate.add(TemplateModel(R.drawable.img_template5))
            listTemplate.add(TemplateModel(R.drawable.img_template6))
            listTemplate.add(TemplateModel(R.drawable.img_template7))
            listTemplate.add(TemplateModel(R.drawable.img_template8))
            listTemplate.add(TemplateModel(R.drawable.img_template9))
            listTemplate.add(TemplateModel(R.drawable.img_template10))
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


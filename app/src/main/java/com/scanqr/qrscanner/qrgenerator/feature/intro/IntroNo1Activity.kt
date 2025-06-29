package com.scanqr.qrscanner.qrgenerator.feature.intro

import android.content.Intent
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Activity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityIntroNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.scanqr.qrscanner.qrgenerator.feature.permission.PermissionNo1Activity
import com.scanqr.qrscanner.qrgenerator.helper.SharePrefHelper
import com.scanqr.qrscanner.qrgenerator.utils.ConstantsNo1.SharePrefKey.IS_SETTING_CONTINUE
import com.scanqr.qrscanner.qrgenerator.utils.tap

class IntroNo1Activity : BaseNo1Activity<ActivityIntroNo1Binding>() {

    private var listData = mutableListOf<IntroModelNo1>()
    private var isFirst = true

    override fun initView() {
        val data = getListIntro()
        binding.vpgSlideIntro.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (isFirst) {
                    isFirst = false
                    return
                }
                addBottomDots(position)
            }
        })
        binding.vpgSlideIntro.adapter = IntroAdapterNo1(data)
        binding.tvNextRight.tap {
            onClickNext()
        }
    }

    private fun onClickNext() {
        if (binding.vpgSlideIntro.currentItem < listData.size - 1) {
            binding.vpgSlideIntro.currentItem += 1
        } else {
            startNextAct()
        }
    }

    private fun startNextAct() {
        if (SharePrefHelper(this).getBoolean(IS_SETTING_CONTINUE)) {
            startActivity(Intent(this, MainNo1Activity::class.java))
            finish()
        } else {
            startActivity(Intent(this, PermissionNo1Activity::class.java))
            finish()
        }
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finishAffinity()
    }

    private fun addBottomDots(currentPage: Int) {
        binding.linearDots.removeAllViews()
        val dots = arrayOfNulls<ImageView>(listData.size)
        for (i in 0 until listData.size) {
            dots[i] = ImageView(this)
            if (i == currentPage)
                dots[i]!!.setImageResource(R.drawable.ic_intro_selected)
            else
                dots[i]!!.setImageResource(R.drawable.ic_intro_unselected)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.linearDots.addView(dots[i], params)
        }
    }


    private fun getListIntro(): MutableList<IntroModelNo1> {
        listData.add(
            IntroModelNo1(
                R.drawable.img_intro_1,
                getString(R.string.intro_desc_1),
                getString(R.string.intro_content_1),
            )
        )
        listData.add(
            IntroModelNo1(
                R.drawable.img_intro_2,
                getString(R.string.intro_desc_2),
                getString(R.string.intro_content_2),
            )
        )
        listData.add(
            IntroModelNo1(
                R.drawable.img_intro_3,
                getString(R.string.intro_desc_3),
                getString(R.string.intro_content_3),
            )
        )
        listData.add(
            IntroModelNo1(
                R.drawable.img_intro_4,
                getString(R.string.customize_your_qr_code),
                getString(R.string.personalize_your_qr_codes_with_colors_logos_and_styles),
            )
        )
        addBottomDots(0)
        return listData
    }

    override fun getViewBinding(): ActivityIntroNo1Binding {
        return ActivityIntroNo1Binding.inflate(layoutInflater)
    }

}
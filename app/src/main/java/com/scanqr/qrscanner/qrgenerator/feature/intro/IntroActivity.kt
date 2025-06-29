package com.scanqr.qrscanner.qrgenerator.feature.intro

import android.content.Intent
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityIntroBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.MainActivity
import com.scanqr.qrscanner.qrgenerator.feature.permission.PermissionActivity
import com.scanqr.qrscanner.qrgenerator.helper.SharePrefHelper
import com.scanqr.qrscanner.qrgenerator.utils.Constants.SharePrefKey.IS_SETTING_CONTINUE
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.invisible
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    private var listData = mutableListOf<IntroModel>()
    private var isFirst = true

    override fun initView() {
        val data = getListIntro()
        Glide.with(this)
            .asGif()
            .load(R.raw.hand_swipe_intro)
            .into(binding.animationView)
        binding.animationView.gone()
        binding.vpgSlideIntro.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (isFirst) {
                    isFirst = false
                    return
                }
                addBottomDots(position)
                when (position) {
                    0 -> {
                        binding.tvNextCenter.visible()
                        binding.tvNextRight.invisible()
                        binding.animationView.gone()
                        binding.view1.visible()
                        binding.view2.gone()
                    }

                    1 -> {
                        binding.tvNextCenter.gone()
                        binding.tvNextRight.visible()
                        binding.animationView.visible()
                        binding.view1.gone()
                        binding.view2.visible()
                    }

                    2 -> {
                        binding.tvNextCenter.gone()
                        binding.tvNextRight.visible()
                        binding.animationView.visible()
                        binding.view1.gone()
                        binding.view2.visible()
                    }

                    3 -> {
                        binding.tvNextCenter.visible()
                        binding.tvNextRight.invisible()
                        binding.animationView.gone()
                        binding.view1.gone()
                        binding.view2.gone()
                    }
                }
            }
        })
        binding.vpgSlideIntro.adapter = IntroAdapter(data)
        binding.tvNextCenter.tap {
            onClickNext()
        }
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, PermissionActivity::class.java))
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


    private fun getListIntro(): MutableList<IntroModel> {
        listData.add(
            IntroModel(
                R.drawable.img_intro_1,
                getString(R.string.intro_desc_1),
                getString(R.string.intro_content_1),
            )
        )
        listData.add(
            IntroModel(
                R.drawable.img_intro_2,
                getString(R.string.intro_desc_2),
                getString(R.string.intro_content_2),
            )
        )
        listData.add(
            IntroModel(
                R.drawable.img_intro_3,
                getString(R.string.intro_desc_3),
                getString(R.string.intro_content_3),
            )
        )
        listData.add(
            IntroModel(
                R.drawable.img_intro_4,
                getString(R.string.customize_your_qr_code),
                getString(R.string.personalize_your_qr_codes_with_colors_logos_and_styles),
            )
        )
        addBottomDots(0)
        return listData
    }

    override fun getViewBinding(): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(layoutInflater)
    }

}
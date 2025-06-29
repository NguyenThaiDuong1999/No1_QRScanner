package com.scanqr.qrscanner.qrgenerator.feature.main.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseNo1Fragment
import com.scanqr.qrscanner.qrgenerator.databinding.FragmentHomeNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.main.create.CreateNo1Activity
import com.scanqr.qrscanner.qrgenerator.feature.main.home.adapter.TypeScanAdapterNo1
import com.scanqr.qrscanner.qrgenerator.feature.main.home.adapter.TypeScanSuggestAdapterNo1
import com.scanqr.qrscanner.qrgenerator.feature.main.model.Type
import com.scanqr.qrscanner.qrgenerator.feature.main.model.TypeScan
import com.scanqr.qrscanner.qrgenerator.feature.scan.ScanNo1Activity
import com.scanqr.qrscanner.qrgenerator.utils.ConstantsNo1
import com.scanqr.qrscanner.qrgenerator.utils.checkCameraPermission
import com.scanqr.qrscanner.qrgenerator.utils.goToSettings
import com.scanqr.qrscanner.qrgenerator.utils.showRationaleDialog
import com.scanqr.qrscanner.qrgenerator.utils.tap

class HomeNo1Fragment : BaseNo1Fragment<FragmentHomeNo1Binding>() {

    private var listSuggest: ArrayList<TypeScan> = arrayListOf()
    private var listOther: ArrayList<TypeScan> = arrayListOf()

    override fun getViewBinding(): FragmentHomeNo1Binding {
        return FragmentHomeNo1Binding.inflate(layoutInflater)
    }

    override fun initView() {
        initListSuggest()
        initListOther()
        binding.rcvSuggest.adapter = TypeScanSuggestAdapterNo1(listSuggest) { type ->
            val intent = Intent(requireContext(), CreateNo1Activity::class.java).apply {
                putExtra(ConstantsNo1.ScreenKey.KEY_SCREEN_INTO_CREATE, type.type.toString())
            }
            startActivity(intent)
        }
        binding.rcvOther.adapter = TypeScanAdapterNo1(listOther) { type ->
            val intent = Intent(requireContext(), CreateNo1Activity::class.java).apply {
                putExtra(ConstantsNo1.ScreenKey.KEY_SCREEN_INTO_CREATE, type.type.toString())
            }
            startActivity(intent)
        }
        binding.cvScan.tap {
            requireActivity().checkCameraPermission(
                onPermissionGranted = {
                    startActivity(Intent(requireContext(), ScanNo1Activity::class.java))
                    requireActivity().finish()
                },
                onShowRationale = {
                    requireActivity().showRationaleDialog(getString(R.string.this_app_need_camera_permission_to_enhance_feature)) {
                        goToSettings()
                    }
                },
                onRequestPermission = {

                }
            )
        }
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                pulseAnimation(binding.icScan)
                handler.postDelayed(this, 1500)
            }
        }
        handler.post(runnable)

    }

    fun pulseAnimation(view: View) {
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f)
        )
        scaleDown.duration = 500

        val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
        )
        scaleUp.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(scaleDown, scaleUp)
        animatorSet.start()
    }

    private fun initListSuggest() {
        listSuggest.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_location,
                getString(R.string.location),
                getString(R.string.text_demo),
                Type.LOCATION,
                "#2962FF"
            )
        )
        listSuggest.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_contact,
                getString(R.string.contact),
                getString(R.string.text_demo),
                Type.CONTACT,
                "#2962FF"
            )
        )
        listSuggest.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_global,
                getString(R.string.url),
                getString(R.string.text_demo),
                Type.URL,
                "#2962FF"
            )
        )
        listSuggest.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_text,
                getString(R.string.text),
                getString(R.string.text_demo),
                Type.TEXT,
                "#2962FF"
            )
        )
        listSuggest.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_barcode,
                getString(R.string.bar_code),
                getString(R.string.text_demo),
                Type.BARCODE,
                "#2962FF"
            )
        )
        listSuggest.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_wifi,
                getString(R.string.wifi),
                getString(R.string.text_demo),
                Type.WIFI,
                "#2962FF"
            )
        )
    }

    private fun initListOther() {
        listOther.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_phone,
                getString(R.string.phone),
                getString(R.string.text_demo),
                Type.PHONE
            )
        )
        listOther.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_email,
                getString(R.string.email),
                getString(R.string.text_demo),
                Type.EMAIL
            )
        )
        listOther.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_sms,
                getString(R.string.sms),
                getString(R.string.text_demo),
                Type.SMS
            )
        )
        listOther.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_facebook,
                getString(R.string.facebook),
                getString(R.string.text_demo),
                Type.FACEBOOK
            )
        )
        listOther.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_instagram,
                getString(R.string.instagram),
                getString(R.string.text_demo),
                Type.INSTAGRAM
            )
        )
        listOther.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_twitter,
                getString(R.string.twitter),
                getString(R.string.text_demo),
                Type.TWITTER
            )
        )
        listOther.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_youtube,
                getString(R.string.youtube),
                getString(R.string.text_demo),
                Type.YOUTUBE
            )
        )
        listOther.add(
            TypeScan(
                "#EBF5FF",
                R.drawable.ic_whatapps,
                getString(R.string.whats_app),
                getString(R.string.text_demo),
                Type.WHATSAPP
            )
        )
    }
}
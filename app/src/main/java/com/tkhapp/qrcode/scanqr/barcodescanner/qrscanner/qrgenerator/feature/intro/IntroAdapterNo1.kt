package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.intro

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.callback.NativeCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ItemGuideNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ItemNativeIntroFullBinding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.RemoteKeys.native_intro_full
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.RemoteKeys.native_intro_full1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.RemoteKeys.native_intro_full1_2
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.Constants.RemoteKeys.native_intro_full_2
import java.util.Locale

class IntroAdapterNo1(
    private val context: Activity,
    private val list: MutableList<IntroModelNo1>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_ADS = 0
    private val TYPE_ITEM = 1
    private var nativeAdFullScreen: NativeAd? = null
    private var nativeAdFullScreen2: NativeAd? = null
    private var nativeAdFullScreen1: NativeAd? = null
    private var nativeAdFullScreen12: NativeAd? = null

    init {
        loadNativeIntroFull(native_intro_full, native_intro_full)
        loadNativeIntroFull(native_intro_full_2, native_intro_full_2)
        loadNativeIntroFull1(native_intro_full1, native_intro_full1)
        loadNativeIntroFull1(native_intro_full1_2, native_intro_full1_2)
    }

    private fun loadNativeIntroFull(adsKey: String, remoteKey: String) {
        Admob.getInstance().loadNativeAds(
            context,
            AdmobApi.getInstance().getListIDByName(adsKey),
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    if (adsKey == native_intro_full) {
                        setNativeAdFullScreen(nativeAd)
                        IntroNo1Activity.nativeAdFullScreen = nativeAd
                    } else if (adsKey == native_intro_full_2) {
                        setNativeAdFullScreen2(nativeAd)
                        IntroNo1Activity.nativeAdFullScreen2 = nativeAd
                    }
                    notifyNativeAdFullScreen()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError?) {
                    super.onAdFailedToLoad(loadAdError)
                    setNativeAdFullScreen(null)
                    setNativeAdFullScreen2(null)
                }
            },
            remoteKey
        )
    }

    private fun loadNativeIntroFull1(adsKey: String, remoteKey: String) {
        Admob.getInstance().loadNativeAds(
            context,
            AdmobApi.getInstance().getListIDByName(adsKey),
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    if (adsKey == native_intro_full1) {
                        setNativeAdFullScreen1(nativeAd)
                        IntroNo1Activity.nativeAdFullScreen1 = nativeAd
                    } else if (adsKey == native_intro_full1_2) {
                        setNativeAdFullScreen12(nativeAd)
                        IntroNo1Activity.nativeAdFullScreen12 = nativeAd
                    }
                    notifyNativeAdFullScreen1()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError?) {
                    super.onAdFailedToLoad(loadAdError)
                    setNativeAdFullScreen1(null)
                    setNativeAdFullScreen12(null)
                }
            },
            remoteKey
        )
    }

    fun setNativeAdFullScreen(nativeAd: NativeAd?) {
        this.nativeAdFullScreen = nativeAd
    }

    fun setNativeAdFullScreen2(nativeAd: NativeAd?) {
        this.nativeAdFullScreen2 = nativeAd
    }

    fun notifyNativeAdFullScreen() {
        notifyItemChanged(1)
    }

    fun setNativeAdFullScreen1(nativeAd: NativeAd?) {
        this.nativeAdFullScreen1 = nativeAd
    }

    fun setNativeAdFullScreen12(nativeAd: NativeAd?) {
        this.nativeAdFullScreen12 = nativeAd
    }

    fun notifyNativeAdFullScreen1() {
        list.forEachIndexed { index, introModel ->
            if (introModel.content == "ads1") {
                notifyItemChanged(index)
            }
        }
    }

    inner class IntroDefaultVH(val binding: ItemGuideNo1Binding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) IntroDefaultVH(
            ItemGuideNo1Binding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
        else NativeFullAdapterHolder(
            ItemNativeIntroFullBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].image == 99) {
            TYPE_ADS
        } else {
            TYPE_ITEM
        }
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NativeFullAdapterHolder) {
            holder.bind(position)
        } else if (holder is IntroDefaultVH) {
            val introModel = list[position]
            holder.binding.apply {
                ivIntro.setImageResource(introModel.image)
                tvDesc.text = introModel.desc
                tvContent.text = introModel.content
            }
        }
    }

    inner class NativeFullAdapterHolder(private var binding: ItemNativeIntroFullBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (list[position].content == "ads") {
                showNativeFullScreen(
                    nativeAdFullScreen,
                    R.layout.ads_native_intro_full_screen,
                    R.layout.ads_native_intro_full_screen,
                    binding.frAds,
                    context
                )
                Handler(context.mainLooper).postDelayed({
                    showNativeFullScreen(
                        nativeAdFullScreen2,
                        R.layout.ads_native_intro_full_screen,
                        R.layout.ads_native_intro_full_screen,
                        binding.frAds,
                        context
                    )
                }, 700)
            } else if (list[position].content == "ads1") {
                showNativeFullScreen(
                    nativeAdFullScreen1,
                    R.layout.ads_native_intro_full_screen,
                    R.layout.ads_native_intro_full_screen,
                    binding.frAds,
                    context
                )
                Handler(context.mainLooper).postDelayed({
                    showNativeFullScreen(
                        nativeAdFullScreen12,
                        R.layout.ads_native_intro_full_screen,
                        R.layout.ads_native_intro_full_screen,
                        binding.frAds,
                        context
                    )
                }, 700)
            }
        }
    }

    private fun showNativeFullScreen(
        nativeAd: NativeAd?,
        layoutNative: Int,
        layoutNativeMeta: Int,
        frAds: ViewGroup,
        context: Activity
    ) {
        val shimmer =
            LayoutInflater.from(context).inflate(R.layout.shimmer_native_full_screen_intro, null)
        frAds.removeAllViews()
        frAds.addView(shimmer)
        if (nativeAd != null) {
            val adView: NativeAdView
            var mediationAdapterClassName: String? = ""
            if (nativeAd.responseInfo != null) {
                mediationAdapterClassName = nativeAd.responseInfo?.mediationAdapterClassName
            }
            if (mediationAdapterClassName != null && mediationAdapterClassName.lowercase(Locale.getDefault())
                    .contains("facebook")
            ) {
                adView =
                    context.layoutInflater.inflate(layoutNativeMeta, frAds, false) as NativeAdView
            } else {
                adView = context.layoutInflater.inflate(layoutNative, frAds, false) as NativeAdView
            }
            frAds.removeAllViews()
            frAds.addView(adView)
            Admob.getInstance().populateNativeAdView(nativeAd, adView)
        }
    }
}
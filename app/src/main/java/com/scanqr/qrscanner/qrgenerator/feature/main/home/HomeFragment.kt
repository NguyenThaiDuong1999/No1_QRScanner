package com.scanqr.qrscanner.qrgenerator.feature.main.home

import android.content.Intent
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseFragment
import com.scanqr.qrscanner.qrgenerator.databinding.FragmentHomeBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.create.CreateActivity
import com.scanqr.qrscanner.qrgenerator.feature.main.home.adapter.TypeScanAdapter
import com.scanqr.qrscanner.qrgenerator.feature.main.home.adapter.TypeScanSuggestAdapter
import com.scanqr.qrscanner.qrgenerator.feature.main.model.Type
import com.scanqr.qrscanner.qrgenerator.feature.main.model.TypeScan
import com.scanqr.qrscanner.qrgenerator.utils.Constants

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var listSuggest: ArrayList<TypeScan> = arrayListOf()
    private var listOther: ArrayList<TypeScan> = arrayListOf()

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        initListSuggest()
        initListOther()
        binding.rcvSuggest.adapter = TypeScanSuggestAdapter(listSuggest) { type ->
            val intent = Intent(requireContext(), CreateActivity::class.java).apply {
                putExtra(Constants.ScreenKey.KEY_SCREEN_INTO_CREATE, type.type.toString())
            }
            startActivity(intent)
        }
        binding.rcvOther.adapter = TypeScanAdapter(listOther) { type ->
            val intent = Intent(requireContext(), CreateActivity::class.java).apply {
                putExtra(Constants.ScreenKey.KEY_SCREEN_INTO_CREATE, type.type.toString())
            }
            startActivity(intent)
        }
    }

    private fun initListSuggest() {
        listSuggest.add(
            TypeScan(
                "#FFF0F0",
                R.drawable.ic_location,
                getString(R.string.location),
                getString(R.string.text_demo),
                Type.LOCATION,
                "#FF1E1E"
            )
        )
        listSuggest.add(
            TypeScan(
                "#DEFEDD",
                R.drawable.ic_contact,
                getString(R.string.contact),
                getString(R.string.text_demo),
                Type.CONTACT,
                "#06C17E"
            )
        )
        listSuggest.add(TypeScan("#EBF5FF", R.drawable.ic_global, getString(R.string.url), getString(R.string.text_demo), Type.URL, "#0A9FFF"))
        listSuggest.add(TypeScan("#EBEAFA", R.drawable.ic_text, getString(R.string.text), getString(R.string.text_demo), Type.TEXT, "#5856D6"))
        listSuggest.add(
            TypeScan(
                "#FFF0DB",
                R.drawable.ic_barcode,
                getString(R.string.bar_code),
                getString(R.string.text_demo),
                Type.BARCODE,
                "#FF9500"
            )
        )
        listSuggest.add(TypeScan("#FFF5EB", R.drawable.ic_wifi, getString(R.string.wifi), getString(R.string.text_demo), Type.WIFI, "#FB6AB0"))
    }

    private fun initListOther() {
        listOther.add(TypeScan("#FEE5E2", R.drawable.ic_phone, getString(R.string.phone), getString(R.string.text_demo), Type.PHONE))
        listOther.add(TypeScan("#E6F0FE", R.drawable.ic_email, getString(R.string.email), getString(R.string.text_demo), Type.EMAIL))
        listOther.add(TypeScan("#D7FEEA", R.drawable.ic_sms, getString(R.string.sms), getString(R.string.text_demo), Type.SMS))
        listOther.add(TypeScan("#E2F1F8", R.drawable.ic_facebook, getString(R.string.facebook), getString(R.string.text_demo), Type.FACEBOOK))
        listOther.add(TypeScan("#FFE5EB", R.drawable.ic_instagram, getString(R.string.instagram), getString(R.string.text_demo), Type.INSTAGRAM))
        listOther.add(TypeScan("#FFF4E5", R.drawable.ic_twitter, getString(R.string.twitter), getString(R.string.text_demo), Type.TWITTER))
        listOther.add(TypeScan("#FFE7E5", R.drawable.ic_youtube, getString(R.string.youtube), getString(R.string.text_demo), Type.YOUTUBE))
        listOther.add(TypeScan("#DCFEED", R.drawable.ic_whatapps, getString(R.string.whats_app), getString(R.string.text_demo), Type.WHATSAPP))
    }
}
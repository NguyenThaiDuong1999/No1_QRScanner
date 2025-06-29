package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.SystemUtilNo1

abstract class BaseNo1Dialog<DB : ViewDataBinding>(activity: Activity, private var canAble: Boolean) :
    Dialog(activity, R.style.BaseDialog) {

    lateinit var mDataBinding: DB

    abstract fun getContentView(): Int
    abstract fun initView()
    abstract fun bindView()

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtilNo1.setLocale(context)
        super.onCreate(savedInstanceState)
        mDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), getContentView(), null, false)
        setContentView(mDataBinding.root)
        setCancelable(canAble)
        initView()
        bindView()
    }

}
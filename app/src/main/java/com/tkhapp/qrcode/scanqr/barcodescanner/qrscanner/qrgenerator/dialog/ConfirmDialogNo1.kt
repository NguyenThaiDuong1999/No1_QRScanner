package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.DialogConfirmNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap

class ConfirmDialogNo1(
    private val title: String,
    private val desc: String,
    private val negativeMsg: String,
    private val positiveMsg: String,
    private val bgNegative: Int,
    private val bgPositive: Int,
    private val onNegative: () -> Unit,
    private val onPositive: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogConfirmNo1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtilNo1.setLocale(requireContext())
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BaseDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfirmNo1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDesc.isSelected = true
        binding.tvTitle.text = title
        binding.tvDesc.text = desc
        binding.tvNegative.text = negativeMsg
        binding.tvPositive.text = positiveMsg
        binding.tvNegative.setBackgroundResource(bgNegative)
        binding.tvPositive.setBackgroundResource(bgPositive)
        binding.tvNegative.tap {
            dismiss()
            onNegative.invoke()
        }
        binding.tvPositive.tap {
            dismiss()
            onPositive.invoke()
        }
    }


}
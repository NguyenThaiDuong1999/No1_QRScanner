package com.scanqr.qrscanner.qrgenerator.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.DialogConfirmBinding
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtil
import com.scanqr.qrscanner.qrgenerator.utils.tap

class ConfirmDialog(
    private val title: String,
    private val desc: String,
    private val negativeMsg: String,
    private val positiveMsg: String,
    private val bgNegative: Int,
    private val bgPositive: Int,
    private val onNegative: () -> Unit,
    private val onPositive: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtil.setLocale(requireContext())
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BaseDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfirmBinding.inflate(layoutInflater)
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
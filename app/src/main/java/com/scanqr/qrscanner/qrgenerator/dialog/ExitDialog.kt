package com.scanqr.qrscanner.qrgenerator.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.DialogExitBinding
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtil
import com.scanqr.qrscanner.qrgenerator.utils.tap

class ExitDialog(
    private val onNegative: () -> Unit,
    private val onPositive: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogExitBinding

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
        binding = DialogExitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDesc.isSelected = true
        binding.tvNegative.tap {
            dismiss()
            onNegative.invoke()
        }
        binding.tvPositive.tap {
            dismiss()
            onPositive.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
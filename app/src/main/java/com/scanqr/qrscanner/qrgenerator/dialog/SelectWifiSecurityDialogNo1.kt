package com.scanqr.qrscanner.qrgenerator.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.DialogSelectWifiSecurityNo1Binding
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class SelectWifiSecurityDialogNo1(private val security: String = "nopass", private val onSelect: (String) -> Unit) : DialogFragment() {

    private lateinit var binding: DialogSelectWifiSecurityNo1Binding

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
        binding = DialogSelectWifiSecurityNo1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            when (security) {
                "nopass" -> {
                    binding.ivSelectNone.visible()
                    binding.ivSelectWep.gone()
                    binding.ivSelectWpa.gone()
                }

                "WPA/WPA2" -> {
                    binding.ivSelectWep.gone()
                    binding.ivSelectNone.gone()
                    binding.ivSelectWpa.visible()
                }

                "WEP" -> {
                    binding.ivSelectNone.gone()
                    binding.ivSelectWpa.gone()
                    binding.ivSelectWep.visible()
                }
            }
            layoutWpa.tap {
                binding.ivSelectNone.gone()
                binding.ivSelectWep.gone()
                binding.ivSelectWpa.visible()
                onSelect.invoke("WPA/WPA2")
                dismiss()
            }
            layoutNone.tap {
                binding.ivSelectNone.visible()
                binding.ivSelectWep.gone()
                binding.ivSelectWpa.gone()
                onSelect.invoke("nopass")
                dismiss()
            }
            layoutWep.tap {
                binding.ivSelectWep.visible()
                binding.ivSelectWpa.gone()
                binding.ivSelectNone.gone()
                onSelect.invoke("WEP")
                dismiss()
            }
        }
    }

}
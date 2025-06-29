package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.include

import android.content.Context
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutCreateWifiNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.dialog.SelectWifiSecurityDialogNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.CreateNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.checkOnEditTextChange
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.setOnFocus
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.showError
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class IncludeCreateWifiHandlerNo1(private val context: Context, private val binding: LayoutCreateWifiNo1Binding) {

    private var security = "WPA/WPA2"

    fun initView() {
        binding.edtWifiName.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharWifiName.text = "$it/30"
                context.setOnFocus(binding.tvErrorWifiName, binding.cvWifiName)
            }
        }
        binding.edtPassword.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharPassword.text = "$it/25"
                context.setOnFocus(binding.tvErrorPassword, binding.cvWifiPassword)
            }
        }
        binding.layoutSecurity.tap {
            val dialog = SelectWifiSecurityDialogNo1(security) {
                security = it
                if (security == "nopass") {
                    binding.tvSecuritySelect.text = context.getString(R.string.none)
                    binding.cvWifiPassword.gone()
                    binding.tvErrorPassword.gone()
                } else {
                    binding.tvSecuritySelect.text = security
                    binding.cvWifiPassword.visible()
                }
            }
            dialog.show((context as CreateNo1Activity).supportFragmentManager, dialog.tag)
        }
    }

    fun onCreateClick(onOK: (String) -> Unit) {
        binding.apply {
            if (edtWifiName.text.toString().trim().isEmpty()) {
                context.showError(tvErrorWifiName, cvWifiName, context.getString(R.string.please_enter_content))
                return
            }
            if (edtWifiName.text.toString().trim().contains(" ")) {
                context.showError(tvErrorWifiName, cvWifiName, context.getString(R.string.wifi_name_must_not_be_contain_space))
                return
            }
            if (edtPassword.text.toString().trim().isEmpty() && security != "nopass") {
                context.showError(tvErrorPassword, cvWifiPassword, context.getString(R.string.please_enter_content))
                return
            }
            if (edtPassword.text.toString().length < 8 && security != "nopass") {
                context.showError(
                    tvErrorPassword,
                    cvWifiPassword,
                    context.getString(R.string.wifi_password_length_must_not_be_shorter_than_eight)
                )
                return
            }
            if (edtPassword.text.toString().trim().contains(" ")) {
                context.showError(tvErrorPassword, cvWifiPassword, context.getString(R.string.wifi_password_must_not_be_contain_space))
                return
            }
            val contentWifi = "WIFI:T:$security;S:${edtWifiName.text.toString().trim()};P:${edtPassword.text};H:;;"
            onOK.invoke(contentWifi)
        }
    }
}
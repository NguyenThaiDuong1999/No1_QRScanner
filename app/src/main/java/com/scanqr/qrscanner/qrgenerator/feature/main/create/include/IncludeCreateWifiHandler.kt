package com.scanqr.qrscanner.qrgenerator.feature.main.create.include

import android.content.Context
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.LayoutCreateWifiBinding
import com.scanqr.qrscanner.qrgenerator.dialog.SelectWifiSecurityDialog
import com.scanqr.qrscanner.qrgenerator.feature.main.create.CreateActivity
import com.scanqr.qrscanner.qrgenerator.utils.checkOnEditTextChange
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.setOnFocus
import com.scanqr.qrscanner.qrgenerator.utils.showError
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class IncludeCreateWifiHandler(private val context: Context, private val binding: LayoutCreateWifiBinding) {

    private var security = "WPA/WPA2"

    fun initView() {
        binding.edtWifiName.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharWifiName.text = "$it/30"
                context.setOnFocus(binding.tvErrorWifiName, binding.cvWifiName)
            }
        }
        binding.edtPassword.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharPassword.text = "$it/25"
                context.setOnFocus(binding.tvErrorPassword, binding.cvWifiPassword)
            }
        }
        binding.layoutSecurity.tap {
            val dialog = SelectWifiSecurityDialog(security) {
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
            dialog.show((context as CreateActivity).supportFragmentManager, dialog.tag)
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
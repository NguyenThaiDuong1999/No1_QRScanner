package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.include

import android.content.Context
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutCreateLocationNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.CreateNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.checkOnEditTextChange
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.setOnFocus
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.showError

class IncludeCreateLocationHandlerNo1(
    private val context: Context,
    private val binding: LayoutCreateLocationNo1Binding
) {

    fun initView() {
        binding.edtLatitude.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharLatitude.text = "$it/100"
                context.setOnFocus(binding.tvErrorLatitude, binding.cvLatitude)
            }
        }

        binding.edtLongitude.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharLongitude.text = "$it/100"
                context.setOnFocus(binding.tvErrorLongitude, binding.cvLongitude)
            }
        }
    }

    fun onCreateClick(onOk: (String) -> Unit) {
        binding.apply {
            if (edtLatitude.text.toString().trim().isEmpty() && edtLongitude.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorLatitude,
                    cvLatitude,
                    context.getString(R.string.please_enter_content)
                )
                context.showError(
                    tvErrorLongitude,
                    cvLongitude,
                    context.getString(R.string.please_enter_content)
                )
                return
            }
            if (edtLatitude.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorLatitude,
                    cvLatitude,
                    context.getString(R.string.please_enter_content)
                )
            } else if (edtLongitude.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorLongitude,
                    cvLongitude,
                    context.getString(R.string.please_enter_content)
                )
            } else if (edtLatitude.text.toString().trim().startsWith(".")) {
                context.showError(
                    tvErrorLatitude,
                    cvLatitude,
                    context.getString(R.string.cant_start_with_char_dot)
                )
            } else if (edtLongitude.text.toString().trim().startsWith(".")) {
                context.showError(
                    tvErrorLongitude,
                    cvLongitude,
                    context.getString(R.string.cant_start_with_char_dot)
                )
            } else if (edtLatitude.text.toString().count { it == '-' } > 1) {
                context.showError(
                    tvErrorLatitude,
                    cvLatitude,
                    context.getString(R.string.cant_have_more_than_one_char_minis)
                )
            } else if (edtLongitude.text.toString().count { it == '-' } > 1) {
                context.showError(
                    tvErrorLongitude,
                    cvLongitude,
                    context.getString(R.string.cant_have_more_than_one_char_minis)
                )
            } else if (edtLatitude.text.toString().contains("-") && !edtLatitude.text.toString()
                    .startsWith("-")
            ) {
                context.showError(
                    tvErrorLatitude,
                    cvLatitude,
                    context.getString(R.string.this_char_minus_must_be_first)
                )
            } else if (edtLongitude.text.toString().contains("-") && !edtLongitude.text.toString()
                    .startsWith("-")
            ) {
                context.showError(
                    tvErrorLongitude,
                    cvLongitude,
                    context.getString(R.string.this_char_minus_must_be_first)
                )
            } else if (edtLatitude.text.toString().count { it == '.' } > 1) {
                context.showError(
                    tvErrorLatitude,
                    cvLatitude,
                    context.getString(R.string.cant_have_more_than_one_char_dot)
                )
            } else if (edtLongitude.text.toString().count { it == '.' } > 1) {
                context.showError(
                    tvErrorLongitude,
                    cvLongitude,
                    context.getString(R.string.cant_have_more_than_one_char_dot)
                )
            } else {
                onOk.invoke("geo:${binding.edtLatitude.text.trim()},${binding.edtLongitude.text.trim()}")
            }
        }
    }
}
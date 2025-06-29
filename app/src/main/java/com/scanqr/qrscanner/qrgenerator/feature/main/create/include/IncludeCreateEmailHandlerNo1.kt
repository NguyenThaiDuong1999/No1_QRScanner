package com.scanqr.qrscanner.qrgenerator.feature.main.create.include

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.Patterns
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.LayoutCreateEmailNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.main.create.CreateNo1Activity
import com.scanqr.qrscanner.qrgenerator.utils.checkOnEditTextChange
import com.scanqr.qrscanner.qrgenerator.utils.setOnFocus
import com.scanqr.qrscanner.qrgenerator.utils.showError

class IncludeCreateEmailHandlerNo1(private val context: Context, private val binding: LayoutCreateEmailNo1Binding) {

    fun initView() {
        binding.edtAddress.filters = arrayOf(InputFilter.LengthFilter(320))
        binding.edtSubject.filters = arrayOf(InputFilter.LengthFilter(500))
        binding.edtContent.filters = arrayOf(InputFilter.LengthFilter(500))
        binding.edtAddress.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharAddress.text = "$it/320"
                context.setOnFocus(binding.tvErrorAddress, binding.cvAddress)
            }
        }
        binding.edtAddress.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.edtSubject.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharSubject.text = "$it/500"
                context.setOnFocus(binding.tvErrorSubject, binding.cvSubject)
            }
        }
        binding.edtSubject.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.edtContent.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharContent.text = "$it/500"
                context.setOnFocus(binding.tvErrorContent, binding.cvContent)
            }
        }
        binding.edtContent.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
    }

    fun onCreateClick(onOK: (String) -> Unit) {
        binding.apply {
            if (edtAddress.text.toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(edtAddress.text.toString()).matches()) {
                context.showError(tvErrorAddress, cvAddress, context.getString(R.string.invalid_email))
                return
            }
            if(edtSubject.text.toString().trim().isEmpty()) {
                context.showError(tvErrorSubject, cvSubject, context.getString(R.string.please_enter_content))
                return
            }
            if(edtContent.text.toString().trim().isEmpty()) {
                context.showError(tvErrorContent, cvContent, context.getString(R.string.please_enter_content))
                return
            }
            if (edtAddress.text.toString().trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(edtAddress.text.toString().trim()).matches()) {
                val contentEmail =
                    "MATMSG:TO:${binding.edtAddress.text.trim()};SUB:${binding.edtSubject.text.trim()};BODY:${binding.edtContent.text.trim()};;"
                onOK.invoke(contentEmail)
            }
        }
    }

}
package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.include

import android.content.Context
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.LayoutCreateSmsNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.CreateNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.checkOnEditTextChange
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.setOnFocus
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.showError

class IncludeCreateMessageHandlerNo1(private val context: Context, private val binding: LayoutCreateSmsNo1Binding) {

    fun initView() {
        binding.edtNumber.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharNumber.text = "$it/20"
            }
            context.setOnFocus(binding.tvErrorNumber, binding.cvNumber)
        }
        binding.edtMessage.checkOnEditTextChange {
            (context as CreateNo1Activity).runOnUiThread {
                binding.tvCountCharMsg.text = "$it/1000"
            }
            context.setOnFocus(binding.tvErrorMsg, binding.cvMsg)
        }
    }

    fun onCreateClick(onOK: (String) -> Unit) {
        if (binding.edtNumber.text.toString().trim().isEmpty()) {
            context.showError(binding.tvErrorNumber, binding.cvNumber, context.getString(R.string.please_enter_content))
        }
        if (binding.edtMessage.text.toString().trim().isEmpty()) {
            context.showError(binding.tvErrorMsg, binding.cvMsg, context.getString(R.string.please_enter_content))
        }
        if (binding.edtNumber.text.toString().trim().isNotEmpty() && binding.edtMessage.text.toString().trim().isNotEmpty()) {
            onOK.invoke("smsto:${binding.edtNumber.text.toString().trim()}:${binding.edtMessage.text}")
        }
    }


}
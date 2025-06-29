package com.scanqr.qrscanner.qrgenerator.feature.main.create.include

import android.content.Context
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.LayoutCreateSmsBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.create.CreateActivity
import com.scanqr.qrscanner.qrgenerator.utils.checkOnEditTextChange
import com.scanqr.qrscanner.qrgenerator.utils.setOnFocus
import com.scanqr.qrscanner.qrgenerator.utils.showError

class IncludeCreateMessageHandler(private val context: Context, private val binding: LayoutCreateSmsBinding) {

    fun initView() {
        binding.edtNumber.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharNumber.text = "$it/20"
            }
            context.setOnFocus(binding.tvErrorNumber, binding.cvNumber)
        }
        binding.edtMessage.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
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
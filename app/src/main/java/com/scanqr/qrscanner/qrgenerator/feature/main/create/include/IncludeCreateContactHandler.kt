package com.scanqr.qrscanner.qrgenerator.feature.main.create.include

import android.content.Context
import android.text.InputType
import android.util.Patterns
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.LayoutCreateContactBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.create.CreateActivity
import com.scanqr.qrscanner.qrgenerator.utils.checkOnEditTextChange
import com.scanqr.qrscanner.qrgenerator.utils.setOnFocus
import com.scanqr.qrscanner.qrgenerator.utils.showError

class IncludeCreateContactHandler(
    private val context: Context,
    private val binding: LayoutCreateContactBinding
) {

    fun initView() {

        binding.edtName.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharName.text = "$it/50"
            }
            context.setOnFocus(binding.tvErrorName, binding.cvName)
        }
        binding.edtPhoneNumber.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharPhoneNumber.text = "$it/20"
            }
            context.setOnFocus(binding.tvErrorPhone, binding.cvPhone)
        }
        binding.edtEmail.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharEmail.text = "$it/320"
            }
            context.setOnFocus(binding.tvErrorEmail, binding.cvEmail)
        }
        binding.edtEmail.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.edtCompany.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharCompany.text = "$it/200"
            }
            context.setOnFocus(binding.tvErrorCompany, binding.cvCompany)
        }
        binding.edtCompany.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.edtJob.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharJob.text = "$it/100"
            }
            context.setOnFocus(binding.tvErrorJob, binding.cvJob)
        }
        binding.edtJob.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.edtAddress.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharAddress.text = "$it/100"
            }
            context.setOnFocus(binding.tvErrorAddress, binding.cvAddress)
        }
        binding.edtAddress.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.edtNote.checkOnEditTextChange {
            (context as CreateActivity).runOnUiThread {
                binding.tvCountCharNote.text = "$it/100"
            }
            context.setOnFocus(binding.tvErrorNote, binding.cvNote)
        }
        binding.edtNote.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
    }

    fun onCreateClick(onOK: (String) -> Unit) {
        binding.apply {
            if (edtName.text.toString().trim().isEmpty()
                && edtEmail.text.toString().trim().isEmpty()
                && edtPhoneNumber.text.toString().trim().isEmpty()
                && edtCompany.text.toString().trim().isEmpty()
                && edtJob.text.toString().trim().isEmpty()
                && edtAddress.text.toString().trim().isEmpty()
                && edtNote.text.toString().trim().isEmpty()
            ) {
                context.showError(
                    tvErrorName,
                    cvName,
                    context.getString(R.string.please_enter_content)
                )
                context.showError(
                    tvErrorEmail,
                    cvEmail,
                    context.getString(R.string.please_enter_content)
                )
                context.showError(
                    tvErrorPhone,
                    cvPhone,
                    context.getString(R.string.please_enter_content)
                )
                context.showError(
                    tvErrorCompany,
                    cvCompany,
                    context.getString(R.string.please_enter_content)
                )
                context.showError(
                    tvErrorJob,
                    cvJob,
                    context.getString(R.string.please_enter_content)
                )
                context.showError(
                    tvErrorAddress,
                    cvAddress,
                    context.getString(R.string.please_enter_content)
                )
                context.showError(
                    tvErrorNote,
                    cvNote,
                    context.getString(R.string.please_enter_content)
                )
                return
            }
            if (edtName.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorName,
                    cvName,
                    context.getString(R.string.please_enter_content)
                )
                return
            }
            if (edtEmail.text.toString().trim().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
                context.showError(tvErrorEmail, cvEmail, context.getString(R.string.invalid_email))
                return
            }
            if (edtPhoneNumber.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorPhone,
                    cvPhone,
                    context.getString(R.string.please_enter_content)
                )
                return
            }
            if (edtEmail.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorEmail,
                    cvEmail,
                    context.getString(R.string.please_enter_content)
                )
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.trim()).matches()) {
                context.showError(
                    tvErrorEmail,
                    cvEmail,
                    context.getString(R.string.invalid_email)
                )
                return
            }
            if (edtCompany.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorCompany,
                    cvCompany,
                    context.getString(R.string.please_enter_content)
                )
                return
            }
            if (edtJob.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorJob,
                    cvJob,
                    context.getString(R.string.please_enter_content)
                )
                return
            }
            if (edtAddress.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorAddress,
                    cvAddress,
                    context.getString(R.string.please_enter_content)
                )
                return
            }
            if (edtNote.text.toString().trim().isEmpty()) {
                context.showError(
                    tvErrorNote,
                    cvNote,
                    context.getString(R.string.please_enter_content)
                )
                return
            }

            val vCard = "BEGIN:VCARD\n" +
                    "VERSION:3.0\n" +
                    "N:;\n" +
                    "FN:${edtName.text.toString().trim()}\n" +
                    "ORG:${edtCompany.text.toString().trim()}\n" +
                    "TITLE:${edtJob.text.trim()}\n" +
                    "ADR:;;${edtAddress.text.trim()};;;;\n" +
                    "TEL;WORk;VOICE:\n" +
                    "TEL;CELL:${edtPhoneNumber.text.trim()}\n" +
                    "TEL;FAX:\n" +
                    "EMAIL;WORK;INTERNET:${edtEmail.text.trim()}\n" +
                    "URL:${edtNote.text.trim()}\n" +
                    "END:VCARD"
            onOK.invoke(vCard)
        }
    }

}
package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.scan.batch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.HistoryModel
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ActivityBatchResultNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.dialog.ConfirmDialogNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.Type
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.TypeUI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.result_scan.ScanResultNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.CONTENT_CONTACT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.CONTENT_EMAIL
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.CONTENT_LOCATION
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.CONTENT_PHONE
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.CONTENT_SMS
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.CONTENT_TEXT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.CONTENT_URL
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.CONTENT_WIFI
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.isLargerTiramisu
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class ScanBatchResultNo1Activity : BaseNo1Activity<ActivityBatchResultNo1Binding>() {

    private var adapter: ScanBatchAdapterNo1? = null
    private var listBatchSelected = arrayListOf<HistoryModel>()
    private var isSelectAll = false

    override fun initView() {
        val listDefaultBatch = arrayListOf<HistoryModel>()
        val listTempBatch = arrayListOf<HistoryModel>()

        binding.icBack.tap {
            val confirmDialogNo1 = ConfirmDialogNo1(
                title = getString(R.string.exit),
                desc = getString(R.string.you_can_review_all_this_data_in_history_tab),
                negativeMsg = getString(R.string.cancel),
                positiveMsg = getString(R.string.exit),
                bgNegative = R.drawable.bg_grey,
                bgPositive = R.drawable.bg_next_action_scan,
                onNegative = {

                },
                onPositive = {
                    finish()
                }
            )
            confirmDialogNo1.show(supportFragmentManager, confirmDialogNo1.tag)
        }
        binding.icCloseBatch.tap {
            binding.icCloseBatch.gone()
            binding.icBack.visible()
            binding.titleSelected.gone()
            binding.titleUnselect.visible()
            binding.tvSelectAll.gone()
            isSelectAll = false
            binding.layoutDelete.gone()
            adapter?.setSelectedModeOff()
        }
        binding.tvSelectAll.tap {
            isSelectAll = true
            listBatchSelected.clear()
            listBatchSelected = listDefaultBatch
            for (i in 0 until listDefaultBatch.size) {
                adapter?.updateSelectedItem(listDefaultBatch[i].contentRawValue)
            }
        }
        binding.layoutDelete.tap {
            Log.i("list", listTempBatch.toString())
            val confirmDialogNo1 = ConfirmDialogNo1(
                title = getString(R.string.delete),
                desc = getString(R.string.are_you_want_to_delete_all),
                negativeMsg = getString(R.string.cancel),
                positiveMsg = getString(R.string.delete),
                bgPositive = R.drawable.bg_red,
                bgNegative = R.drawable.bg_grey,
                onNegative = {

                },
                onPositive = {
                    binding.layoutDelete.gone()
                    for (i in 0 until listBatchSelected.size) {
                        listTempBatch.remove(listBatchSelected.find { it.contentRawValue == listBatchSelected[i].contentRawValue })
                        if (listTempBatch.size == 0) {
                            binding.layoutEmpty.visible()
                            binding.rcvBatchResult.gone()
                        } else {
                            adapter?.updateList(listTempBatch)
                        }
                    }
                    closeSelect()
                }
            )
            confirmDialogNo1.show(supportFragmentManager, confirmDialogNo1.tag)
        }
        val size = intent.extras?.getInt("size_batch")!!
        for (i in 0 until size) {
            val item = if (isLargerTiramisu()) {
                intent.extras!!.getSerializable("item_batch_$i", HistoryModel::class.java)
            } else {
                intent.extras!!.getSerializable("item_batch_$i") as HistoryModel
            }
            if (item != null) {
                listDefaultBatch.add(item)
                listTempBatch.add(item)
            }
        }
        adapter = ScanBatchAdapterNo1(
            listTempBatch,
            onItemClick = { item ->
                when (item.codeType) {
                    Type.URL.toString() -> goToResult(item.contentRawValue, CONTENT_URL, Type.URL, TypeUI.SINGLE)
                    Type.TEXT.toString() -> goToResult(item.contentRawValue, CONTENT_TEXT, Type.TEXT, TypeUI.SINGLE)
                    Type.LOCATION.toString() -> goToResult(item.contentRawValue, CONTENT_LOCATION, Type.LOCATION, TypeUI.MULTIPLE)
                    Type.CONTACT.toString() -> goToResult(item.contentRawValue, CONTENT_CONTACT, Type.CONTACT, TypeUI.MULTIPLE)
                    Type.WIFI.toString() -> goToResult(item.contentRawValue, CONTENT_WIFI, Type.WIFI, TypeUI.MULTIPLE)
                    Type.PHONE.toString() -> goToResult(item.contentRawValue, CONTENT_PHONE, Type.PHONE, TypeUI.SINGLE)
                    Type.SMS.toString() -> goToResult(item.contentRawValue, CONTENT_SMS, Type.SMS, TypeUI.MULTIPLE)
                    Type.EMAIL.toString() -> goToResult(item.contentRawValue, CONTENT_EMAIL, Type.EMAIL, TypeUI.MULTIPLE)
                    Type.BARCODE.toString() -> goToResult(item.contentRawValue, CONTENT_TEXT, Type.BARCODE, TypeUI.BARCODE)
                    Type.FACEBOOK.toString() -> goToResult(item.contentRawValue, CONTENT_URL, Type.FACEBOOK, TypeUI.SINGLE)
                    Type.YOUTUBE.toString() -> goToResult(item.contentRawValue, CONTENT_URL, Type.YOUTUBE, TypeUI.SINGLE)
                    Type.INSTAGRAM.toString() -> goToResult(item.contentRawValue, CONTENT_URL, Type.INSTAGRAM, TypeUI.SINGLE)
                    Type.WHATSAPP.toString() -> goToResult(item.contentRawValue, CONTENT_URL, Type.WHATSAPP, TypeUI.SINGLE)
                    Type.TWITTER.toString() -> goToResult(item.contentRawValue, CONTENT_URL, Type.TWITTER, TypeUI.SINGLE)
                }
            },
            onItemLongClick = { item, pos ->
                listBatchSelected.add(item)
                binding.layoutDelete.visible()
                binding.icBack.gone()
                binding.icCloseBatch.visible()
                binding.titleSelected.visible()
                binding.titleUnselect.gone()
                binding.tvSelectAll.visible()
                adapter?.setSelectedModeOn()
                adapter?.updateSelectedItem(item.contentRawValue)
            },
            onDelete = { item, pos ->
                val confirmDialogNo1 = ConfirmDialogNo1(
                    title = getString(R.string.delete),
                    desc = getString(R.string.are_you_want_to_delete_this),
                    negativeMsg = getString(R.string.cancel),
                    positiveMsg = getString(R.string.delete),
                    bgPositive = R.drawable.bg_red,
                    bgNegative = R.drawable.bg_grey,
                    onNegative = {

                    },
                    onPositive = {
                        listTempBatch.removeAt(pos)
                        adapter?.updateList(listTempBatch)
                        if (listTempBatch.size == 0) {
                            binding.layoutEmpty.visible()
                            binding.rcvBatchResult.gone()
                        }
                    }
                )
                confirmDialogNo1.show(supportFragmentManager, confirmDialogNo1.tag)
            },
            onSelect = { item, pos ->
                listBatchSelected.add(item)
            },
            onUnselect = { item, pos ->
                isSelectAll = false
                listBatchSelected.remove(listBatchSelected.find { it.id == item.id })
            })
        binding.rcvBatchResult.adapter = adapter
    }

    private fun goToResult(content: String, keyData: String, type: Type, uiType: TypeUI) {
        val bundle = Bundle().apply {
            putString(ConstantsNo1.ScreenKey.KEY_SCREEN_TO_RESULT, "batch")
            putString(ConstantsNo1.ScreenKey.CODE_TYPE, type.toString())
            putString(ConstantsNo1.ScreenKey.UI_CODE_TYPE, uiType.toString())
            putString(keyData, content)
        }
        val intent = Intent(this, ScanResultNo1Activity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finish()
    }

    override fun getViewBinding(): ActivityBatchResultNo1Binding {
        return ActivityBatchResultNo1Binding.inflate(layoutInflater)
    }

    private fun closeSelect() {
        adapter?.setSelectedModeOff()
        binding.titleUnselect.visible()
        binding.icBack.visible()
        binding.icCloseBatch.gone()
        binding.titleSelected.gone()
        binding.tvSelectAll.gone()
        binding.layoutDelete.gone()
        listBatchSelected.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        listBatchSelected.clear()
    }

}
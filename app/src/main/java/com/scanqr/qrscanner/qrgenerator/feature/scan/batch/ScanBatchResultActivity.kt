package com.scanqr.qrscanner.qrgenerator.feature.scan.batch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseActivity
import com.scanqr.qrscanner.qrgenerator.database.HistoryModel
import com.scanqr.qrscanner.qrgenerator.databinding.ActivityBatchResultBinding
import com.scanqr.qrscanner.qrgenerator.dialog.ConfirmDialog
import com.scanqr.qrscanner.qrgenerator.feature.main.model.Type
import com.scanqr.qrscanner.qrgenerator.feature.main.model.TypeUI
import com.scanqr.qrscanner.qrgenerator.feature.result_scan.ScanResultActivity
import com.scanqr.qrscanner.qrgenerator.utils.Constants
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_CONTACT
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_EMAIL
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_LOCATION
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_PHONE
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_SMS
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_TEXT
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_URL
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.CONTENT_WIFI
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.isLargerTiramisu
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class ScanBatchResultActivity : BaseActivity<ActivityBatchResultBinding>() {

    private var adapter: ScanBatchAdapter? = null
    private var listBatchSelected = arrayListOf<HistoryModel>()
    private var isSelectAll = false

    override fun initView() {
        val listDefaultBatch = arrayListOf<HistoryModel>()
        val listTempBatch = arrayListOf<HistoryModel>()

        binding.icBack.tap {
            val confirmDialog = ConfirmDialog(
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
            confirmDialog.show(supportFragmentManager, confirmDialog.tag)
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
            val confirmDialog = ConfirmDialog(
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
            confirmDialog.show(supportFragmentManager, confirmDialog.tag)
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
        adapter = ScanBatchAdapter(
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
                val confirmDialog = ConfirmDialog(
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
                confirmDialog.show(supportFragmentManager, confirmDialog.tag)
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
            putString(Constants.ScreenKey.KEY_SCREEN_TO_RESULT, "batch")
            putString(Constants.ScreenKey.CODE_TYPE, type.toString())
            putString(Constants.ScreenKey.UI_CODE_TYPE, uiType.toString())
            putString(keyData, content)
        }
        val intent = Intent(this, ScanResultActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun initData() {

    }

    override fun onBackPressCustom() {
        finish()
    }

    override fun getViewBinding(): ActivityBatchResultBinding {
        return ActivityBatchResultBinding.inflate(layoutInflater)
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
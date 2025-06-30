package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.history

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.base.BaseNo1Fragment
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.AppDatabaseNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.HistoryModel
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.HistoryType
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.FragmentHistoryNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.dialog.ConfirmDialogNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.MainNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.Type
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.result_scan.ScanResultNo1Activity
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.ConstantsNo1.ScreenKey.KEY_SCREEN_TO_RESULT
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.clearCacheDirectory
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.copyText
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.generate128Barcode
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.generateQRCode
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.saveBitmapAsImageToCache
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.shareCode
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.shareCodeIfSavedToCache
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.shareText
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryNo1Fragment : BaseNo1Fragment<FragmentHistoryNo1Binding>() {

    private var listScanned = mutableListOf<HistoryModel>()
    private var listCreated = mutableListOf<HistoryModel>()
    private var historyAdapterNo1: HistoryAdapterNo1? = null
    private var tempPath = ""
    private lateinit var layoutMainFooter: ConstraintLayout
    private lateinit var layoutDelete: LinearLayout
    private var historyType = HistoryType.SCANNED
    private var listScannedSelected = mutableListOf<HistoryModel>()
    private var listCreatedSelected = mutableListOf<HistoryModel>()
    private var isSelectAll = false

    override fun getViewBinding(): FragmentHistoryNo1Binding {
        return FragmentHistoryNo1Binding.inflate(layoutInflater)
    }

    override fun initView() {
        setSelectedScanned()
        binding.layoutScanned.tap {
            setSelectedScanned()
        }
        binding.layoutCreated.tap {
            setSelectedCreated()
        }
        layoutMainFooter = (activity as MainNo1Activity).findViewById(R.id.footer)
        layoutDelete = (activity as MainNo1Activity).findViewById(R.id.layout_delete)
        binding.tvSelectAll.tap {
            isSelectAll = true
            when (historyType) {
                HistoryType.SCANNED -> {
                    listScannedSelected.clear()
                    listScannedSelected = AppDatabaseNo1.getInstance(requireContext()).appDao()
                        .getAllHistoryByType(HistoryType.SCANNED)
                }

                HistoryType.CREATED -> {
                    listCreatedSelected.clear()
                    listCreatedSelected = AppDatabaseNo1.getInstance(requireContext()).appDao()
                        .getAllHistoryByType(HistoryType.CREATED)
                }
            }
            historyAdapterNo1?.updateSelectAll()
            historyAdapterNo1?.setSelectedModeOn()
        }

        layoutDelete.tap {
            when (historyType) {
                HistoryType.SCANNED -> {
                    val title = if (historyAdapterNo1?.isSelectAll() == true) getString(R.string.delete_all) else getString(R.string.delete)
                    val desc = if (historyAdapterNo1?.isSelectAll() == true) getString(R.string.are_you_want_to_delete_all) else getString(R.string.are_you_want_to_delete_this)
                    val confirmDialogNo1 = ConfirmDialogNo1(
                        title = title,
                        desc = desc,
                        negativeMsg = getString(R.string.cancel),
                        positiveMsg = getString(R.string.delete),
                        bgNegative = R.drawable.bg_grey,
                        bgPositive = R.drawable.bg_red,
                        onNegative = {
                            // do nothing
                        },
                        onPositive = {
                            CoroutineScope(Dispatchers.IO).launch {
                                for (item in listScannedSelected) {
                                    AppDatabaseNo1.getInstance(requireContext()).appDao()
                                        .deleteHistory(item.id)
                                }
                            }.invokeOnCompletion {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.delete_successfully),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    closeSelection()
                                    if (AppDatabaseNo1.getInstance(requireContext()).appDao()
                                            .getCountSizeByType(HistoryType.SCANNED) == 0
                                    ) {
                                        binding.layoutEmpty.visible()
                                        binding.rcvHistory.gone()
                                    } else {
                                        historyAdapterNo1?.updateList(
                                            AppDatabaseNo1.getInstance(
                                                requireContext()
                                            ).appDao().getAllHistoryByType(HistoryType.SCANNED)
                                        )
                                    }
                                }
                            }
                        }
                    )
                    confirmDialogNo1.show(childFragmentManager, confirmDialogNo1.tag)
                }

                HistoryType.CREATED -> {
                    val title = if (historyAdapterNo1?.isSelectAll() == true) getString(R.string.delete_all) else getString(R.string.delete)
                    val desc = if (historyAdapterNo1?.isSelectAll() == true) getString(R.string.are_you_want_to_delete_all) else getString(R.string.are_you_want_to_delete_this)
                    val confirmDialogNo1 = ConfirmDialogNo1(
                        title = title,
                        desc = desc,
                        negativeMsg = getString(R.string.cancel),
                        positiveMsg = getString(R.string.delete),
                        bgNegative = R.drawable.bg_grey,
                        bgPositive = R.drawable.bg_red,
                        onNegative = {
                            // do nothing
                        },
                        onPositive = {
                            CoroutineScope(Dispatchers.IO).launch {
                                for (item in listCreatedSelected) {
                                    AppDatabaseNo1.getInstance(requireContext()).appDao()
                                        .deleteHistory(item.id)
                                }
                            }.invokeOnCompletion {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.delete_successfully),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    closeSelection()
                                    if (AppDatabaseNo1.getInstance(requireContext()).appDao()
                                            .getCountSizeByType(HistoryType.CREATED) == 0
                                    ) {
                                        binding.layoutEmpty.visible()
                                        binding.rcvHistory.gone()
                                    } else {
                                        historyAdapterNo1?.updateList(
                                            AppDatabaseNo1.getInstance(
                                                requireContext()
                                            ).appDao().getAllHistoryByType(HistoryType.CREATED)
                                        )
                                    }
                                }
                            }
                        }
                    )
                    confirmDialogNo1.show(childFragmentManager, confirmDialogNo1.tag)
                }
            }
        }

        binding.ivClose.tap {
            closeSelection()
        }
    }

    private fun closeSelection() {
        if (historyType == HistoryType.SCANNED) {
            listScannedSelected.clear()
        } else if (historyType == HistoryType.CREATED) {
            listCreatedSelected.clear()
        }
        layoutDelete.gone()
        layoutMainFooter.visible()
        binding.layoutUnselect.visible()
        binding.layoutSelected.gone()
        historyAdapterNo1?.setSelectedModeOff()
        historyAdapterNo1?.updateUnselectAll()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapterListCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            listCreated = AppDatabaseNo1.getInstance(requireContext()).appDao()
                .getAllHistoryByType(HistoryType.CREATED)
            listCreated.reverse()
        }.invokeOnCompletion {
            requireActivity().runOnUiThread {
                historyAdapterNo1 = HistoryAdapterNo1(listCreated,
                    onItemClick = {
                        gotoResult(it)
                    },
                    onItemLongClick = {
                        historyAdapterNo1?.setSelectedModeOn()
                        historyAdapterNo1?.updateSelectedItem(it.id)
                        listCreatedSelected.add(it)
                        binding.layoutSelected.visible()
                        binding.layoutUnselect.gone()
                        layoutMainFooter.gone()
                        layoutDelete.visible()
                    },
                    onCopy = {
                        requireContext().copyText("", it.contentRawValue)
                    },
                    onShare = { history ->
                        val bitmap = if (history.codeType == Type.BARCODE.toString())
                            generate128Barcode(
                                history.contentRawValue,
                                600,
                                300
                            ) else generateQRCode(history.contentRawValue, 512, 512)
                        requireActivity().saveBitmapAsImageToCache(bitmap!!, history.timeDone,
                            onSuccess = { path ->
                                tempPath = path
                                requireActivity().shareCode(path)
                            },
                            onSaved = {
                                tempPath = requireActivity().shareCodeIfSavedToCache(timeDone = history.timeDone)
                                requireActivity().shareCodeIfSavedToCache(timeDone = history.timeDone)
                            })

                    },
                    onDelete = { history, _ ->
                        val confirmDialogNo1 = ConfirmDialogNo1(
                            title = getString(R.string.delete),
                            desc = getString(R.string.are_you_want_to_delete_this),
                            negativeMsg = getString(R.string.cancel),
                            positiveMsg = getString(R.string.delete),
                            bgNegative = R.drawable.bg_grey,
                            bgPositive = R.drawable.bg_red,
                            onNegative = {
                                // do nothing
                            },
                            onPositive = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    AppDatabaseNo1.getInstance(requireContext()).appDao()
                                        .deleteHistory(history.id)
                                }.invokeOnCompletion {
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.delete_successfully),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        historyAdapterNo1?.updateList(
                                            AppDatabaseNo1.getInstance(
                                                requireContext()
                                            ).appDao().getAllHistoryByType(HistoryType.CREATED)
                                        )
                                        if (AppDatabaseNo1.getInstance(requireContext()).appDao()
                                                .getCountSizeByType(HistoryType.CREATED) == 0
                                        ) {
                                            binding.layoutEmpty.visible()
                                            binding.rcvHistory.gone()
                                        }
                                    }
                                }
                            }
                        )
                        confirmDialogNo1.show(childFragmentManager, confirmDialogNo1.tag)
                    },
                    onSelect = {
                        listCreatedSelected.add(it)
                    },
                    onUnselect = { history ->
                        isSelectAll = false
                        listCreatedSelected.remove(listCreatedSelected.find { it.id == history.id })
                    })
                binding.rcvHistory.adapter = historyAdapterNo1
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapterListScan() {
        CoroutineScope(Dispatchers.IO).launch {
            listScanned = AppDatabaseNo1.getInstance(requireContext()).appDao()
                .getAllHistoryByType(HistoryType.SCANNED)
            listScanned.reverse()
        }.invokeOnCompletion {
            requireActivity().runOnUiThread {
                historyAdapterNo1 = HistoryAdapterNo1(listScanned,
                    onItemClick = { item ->
                        gotoResult(item)
                    },
                    onItemLongClick = {
                        historyAdapterNo1?.setSelectedModeOn()
                        historyAdapterNo1?.updateSelectedItem(it.id)
                        listScannedSelected.add(it)
                        binding.layoutSelected.visible()
                        binding.layoutUnselect.gone()
                        layoutMainFooter.gone()
                        layoutDelete.visible()
                    },
                    onCopy = {
                        requireContext().copyText("", it.contentRawValue)
                    },
                    onShare = { history ->
                        requireActivity().shareText(history.contentRawValue)
                    },
                    onDelete = { history, pos ->
                        val confirmDialogNo1 = ConfirmDialogNo1(
                            title = getString(R.string.delete),
                            desc = getString(R.string.are_you_want_to_delete_this),
                            negativeMsg = getString(R.string.cancel),
                            positiveMsg = getString(R.string.delete),
                            bgNegative = R.drawable.bg_grey,
                            bgPositive = R.drawable.bg_red,
                            onNegative = {
                                // do nothing
                            },
                            onPositive = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    AppDatabaseNo1.getInstance(requireContext()).appDao()
                                        .deleteHistory(history.id)
                                }.invokeOnCompletion {
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.delete_successfully),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        historyAdapterNo1?.updateList(
                                            AppDatabaseNo1.getInstance(
                                                requireContext()
                                            ).appDao().getAllHistoryByType(HistoryType.SCANNED)
                                        )
                                        if (AppDatabaseNo1.getInstance(requireContext()).appDao()
                                                .getCountSizeByType(HistoryType.SCANNED) == 0
                                        ) {
                                            binding.layoutEmpty.visible()
                                            binding.rcvHistory.gone()
                                        }
                                    }
                                }
                            }
                        )
                        confirmDialogNo1.show(childFragmentManager, confirmDialogNo1.tag)
                    },
                    onSelect = {
                        listScannedSelected.add(it)
                    },
                    onUnselect = { history ->
                        isSelectAll = false
                        listScannedSelected.remove(listScannedSelected.find { it.id == history.id })
                    })
                binding.rcvHistory.adapter = historyAdapterNo1
            }
        }
    }

    private fun gotoResult(item: HistoryModel) {
        val bundle = Bundle().apply {
            putString(KEY_SCREEN_TO_RESULT, "history")
            putString(ConstantsNo1.ScreenKey.CODE_TYPE, item.codeType)
            putString(ConstantsNo1.ScreenKey.UI_CODE_TYPE, item.uiType)
            when (item.codeType) {
                Type.TEXT.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_TEXT,
                    item.contentRawValue
                )

                Type.URL.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_URL,
                    item.contentRawValue
                )

                Type.WIFI.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_WIFI,
                    item.contentRawValue
                )

                Type.EMAIL.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_EMAIL,
                    item.contentRawValue
                )

                Type.SMS.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_SMS,
                    item.contentRawValue
                )

                Type.PHONE.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_PHONE,
                    item.contentRawValue
                )

                Type.LOCATION.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_LOCATION,
                    item.contentRawValue
                )

                Type.CONTACT.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_CONTACT,
                    item.contentRawValue
                )

                Type.BARCODE.toString() -> putString(
                    ConstantsNo1.ScreenKey.CONTENT_TEXT,
                    item.contentRawValue
                )

                Type.FACEBOOK.toString(), Type.YOUTUBE.toString(), Type.INSTAGRAM.toString(), Type.TWITTER.toString(), Type.WHATSAPP.toString()
                    -> putString(ConstantsNo1.ScreenKey.CONTENT_URL, item.contentRawValue)
            }
        }
        val intent = Intent(requireContext(), ScanResultNo1Activity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        clearCacheDirectory(requireActivity(), "scan_qr")
    }

    private fun setSelectedScanned() {
        binding.tvScan.setBackgroundResource(R.drawable.bg_btn_create_qr)
        binding.tvCreate.setBackgroundResource(R.drawable.bg_history_non_select)
        historyType = HistoryType.SCANNED
        if (AppDatabaseNo1.getInstance(requireContext()).appDao()
                .getCountSizeByType(HistoryType.SCANNED) == 0
        ) {
            binding.rcvHistory.gone()
            binding.layoutEmpty.visible()
        } else {
            binding.layoutEmpty.gone()
            binding.rcvHistory.visible()
            setAdapterListScan()
        }
    }

    private fun setSelectedCreated() {
        binding.tvCreate.setBackgroundResource(R.drawable.bg_btn_create_qr)
        binding.tvScan.setBackgroundResource(R.drawable.bg_history_non_select)
        historyType = HistoryType.CREATED
        if (AppDatabaseNo1.getInstance(requireContext()).appDao()
                .getCountSizeByType(HistoryType.CREATED) == 0
        ) {
            binding.rcvHistory.gone()
            binding.layoutEmpty.visible()
        } else {
            binding.layoutEmpty.gone()
            binding.rcvHistory.visible()
            setAdapterListCreate()
        }
    }


}
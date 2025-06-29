package com.scanqr.qrscanner.qrgenerator.feature.main.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.base.BaseFragment
import com.scanqr.qrscanner.qrgenerator.database.AppDatabase
import com.scanqr.qrscanner.qrgenerator.database.HistoryModel
import com.scanqr.qrscanner.qrgenerator.database.HistoryType
import com.scanqr.qrscanner.qrgenerator.databinding.FragmentHistoryBinding
import com.scanqr.qrscanner.qrgenerator.dialog.ConfirmDialog
import com.scanqr.qrscanner.qrgenerator.feature.main.MainActivity
import com.scanqr.qrscanner.qrgenerator.feature.main.model.Type
import com.scanqr.qrscanner.qrgenerator.feature.result_scan.ScanResultActivity
import com.scanqr.qrscanner.qrgenerator.utils.Constants
import com.scanqr.qrscanner.qrgenerator.utils.Constants.ScreenKey.KEY_SCREEN_TO_RESULT
import com.scanqr.qrscanner.qrgenerator.utils.clearCacheDirectory
import com.scanqr.qrscanner.qrgenerator.utils.copyText
import com.scanqr.qrscanner.qrgenerator.utils.generate128Barcode
import com.scanqr.qrscanner.qrgenerator.utils.generateQRCode
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.saveBitmapAsImageToCache
import com.scanqr.qrscanner.qrgenerator.utils.shareCode
import com.scanqr.qrscanner.qrgenerator.utils.shareCodeIfSavedToCache
import com.scanqr.qrscanner.qrgenerator.utils.shareText
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private var listScanned = mutableListOf<HistoryModel>()
    private var listCreated = mutableListOf<HistoryModel>()
    private var historyAdapter: HistoryAdapter? = null
    private var tempPath = ""
    private lateinit var layoutMainFooter: ConstraintLayout
    private lateinit var layoutDelete: LinearLayout
    private var historyType = HistoryType.SCANNED
    private var listScannedSelected = mutableListOf<HistoryModel>()
    private var listCreatedSelected = mutableListOf<HistoryModel>()
    private var isSelectAll = false

    override fun getViewBinding(): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(layoutInflater)
    }

    override fun initView() {
        setSelectedScanned()
        binding.layoutScanned.tap {
            setSelectedScanned()
        }
        binding.layoutCreated.tap {
            setSelectedCreated()
        }
        layoutMainFooter = (activity as MainActivity).findViewById(R.id.footer)
        layoutDelete = (activity as MainActivity).findViewById(R.id.layout_delete)
        binding.tvSelectAll.tap {
            isSelectAll = true
            when (historyType) {
                HistoryType.SCANNED -> {
                    listScannedSelected.clear()
                    listScannedSelected = AppDatabase.getInstance(requireContext()).appDao()
                        .getAllHistoryByType(HistoryType.SCANNED)
                }

                HistoryType.CREATED -> {
                    listCreatedSelected.clear()
                    listCreatedSelected = AppDatabase.getInstance(requireContext()).appDao()
                        .getAllHistoryByType(HistoryType.CREATED)
                }
            }
            historyAdapter?.updateSelectAll()
            historyAdapter?.setSelectedModeOn()
        }

        layoutDelete.tap {
            when (historyType) {
                HistoryType.SCANNED -> {
                    val title = if (historyAdapter?.isSelectAll() == true) getString(R.string.delete_all) else getString(R.string.delete)
                    val desc = if (historyAdapter?.isSelectAll() == true) getString(R.string.are_you_want_to_delete_all) else getString(R.string.are_you_want_to_delete_this)
                    val confirmDialog = ConfirmDialog(
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
                                    AppDatabase.getInstance(requireContext()).appDao()
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
                                    if (AppDatabase.getInstance(requireContext()).appDao()
                                            .getCountSizeByType(HistoryType.SCANNED) == 0
                                    ) {
                                        binding.layoutEmpty.visible()
                                        binding.rcvHistory.gone()
                                    } else {
                                        historyAdapter?.updateList(
                                            AppDatabase.getInstance(
                                                requireContext()
                                            ).appDao().getAllHistoryByType(HistoryType.SCANNED)
                                        )
                                    }
                                }
                            }
                        }
                    )
                    confirmDialog.show(childFragmentManager, confirmDialog.tag)
                }

                HistoryType.CREATED -> {
                    val title = if (historyAdapter?.isSelectAll() == true) getString(R.string.delete_all) else getString(R.string.delete)
                    val desc = if (historyAdapter?.isSelectAll() == true) getString(R.string.are_you_want_to_delete_all) else getString(R.string.are_you_want_to_delete_this)
                    val confirmDialog = ConfirmDialog(
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
                                    AppDatabase.getInstance(requireContext()).appDao()
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
                                    if (AppDatabase.getInstance(requireContext()).appDao()
                                            .getCountSizeByType(HistoryType.CREATED) == 0
                                    ) {
                                        binding.layoutEmpty.visible()
                                        binding.rcvHistory.gone()
                                    } else {
                                        historyAdapter?.updateList(
                                            AppDatabase.getInstance(
                                                requireContext()
                                            ).appDao().getAllHistoryByType(HistoryType.CREATED)
                                        )
                                    }
                                }
                            }
                        }
                    )
                    confirmDialog.show(childFragmentManager, confirmDialog.tag)
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
        historyAdapter?.setSelectedModeOff()
        historyAdapter?.updateUnselectAll()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapterListCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            listCreated = AppDatabase.getInstance(requireContext()).appDao()
                .getAllHistoryByType(HistoryType.CREATED)
            listCreated.reverse()
        }.invokeOnCompletion {
            requireActivity().runOnUiThread {
                historyAdapter = HistoryAdapter(listCreated,
                    onItemClick = {
                        gotoResult(it)
                    },
                    onItemLongClick = {
                        historyAdapter?.setSelectedModeOn()
                        historyAdapter?.updateSelectedItem(it.id)
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
                        val confirmDialog = ConfirmDialog(
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
                                    AppDatabase.getInstance(requireContext()).appDao()
                                        .deleteHistory(history.id)
                                }.invokeOnCompletion {
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.delete_successfully),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        historyAdapter?.updateList(
                                            AppDatabase.getInstance(
                                                requireContext()
                                            ).appDao().getAllHistoryByType(HistoryType.CREATED)
                                        )
                                        if (AppDatabase.getInstance(requireContext()).appDao()
                                                .getCountSizeByType(HistoryType.CREATED) == 0
                                        ) {
                                            binding.layoutEmpty.visible()
                                            binding.rcvHistory.gone()
                                        }
                                    }
                                }
                            }
                        )
                        confirmDialog.show(childFragmentManager, confirmDialog.tag)
                    },
                    onSelect = {
                        listCreatedSelected.add(it)
                    },
                    onUnselect = { history ->
                        isSelectAll = false
                        listCreatedSelected.remove(listCreatedSelected.find { it.id == history.id })
                    })
                binding.rcvHistory.adapter = historyAdapter
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapterListScan() {
        CoroutineScope(Dispatchers.IO).launch {
            listScanned = AppDatabase.getInstance(requireContext()).appDao()
                .getAllHistoryByType(HistoryType.SCANNED)
            listScanned.reverse()
        }.invokeOnCompletion {
            requireActivity().runOnUiThread {
                historyAdapter = HistoryAdapter(listScanned,
                    onItemClick = { item ->
                        gotoResult(item)
                    },
                    onItemLongClick = {
                        historyAdapter?.setSelectedModeOn()
                        historyAdapter?.updateSelectedItem(it.id)
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
                        val confirmDialog = ConfirmDialog(
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
                                    AppDatabase.getInstance(requireContext()).appDao()
                                        .deleteHistory(history.id)
                                }.invokeOnCompletion {
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.delete_successfully),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        historyAdapter?.updateList(
                                            AppDatabase.getInstance(
                                                requireContext()
                                            ).appDao().getAllHistoryByType(HistoryType.SCANNED)
                                        )
                                        if (AppDatabase.getInstance(requireContext()).appDao()
                                                .getCountSizeByType(HistoryType.SCANNED) == 0
                                        ) {
                                            binding.layoutEmpty.visible()
                                            binding.rcvHistory.gone()
                                        }
                                    }
                                }
                            }
                        )
                        confirmDialog.show(childFragmentManager, confirmDialog.tag)
                    },
                    onSelect = {
                        listScannedSelected.add(it)
                    },
                    onUnselect = { history ->
                        isSelectAll = false
                        listScannedSelected.remove(listScannedSelected.find { it.id == history.id })
                    })
                binding.rcvHistory.adapter = historyAdapter
            }
        }
    }

    private fun gotoResult(item: HistoryModel) {
        val bundle = Bundle().apply {
            putString(KEY_SCREEN_TO_RESULT, "history")
            putString(Constants.ScreenKey.CODE_TYPE, item.codeType)
            putString(Constants.ScreenKey.UI_CODE_TYPE, item.uiType)
            when (item.codeType) {
                Type.TEXT.toString() -> putString(
                    Constants.ScreenKey.CONTENT_TEXT,
                    item.contentRawValue
                )

                Type.URL.toString() -> putString(
                    Constants.ScreenKey.CONTENT_URL,
                    item.contentRawValue
                )

                Type.WIFI.toString() -> putString(
                    Constants.ScreenKey.CONTENT_WIFI,
                    item.contentRawValue
                )

                Type.EMAIL.toString() -> putString(
                    Constants.ScreenKey.CONTENT_EMAIL,
                    item.contentRawValue
                )

                Type.SMS.toString() -> putString(
                    Constants.ScreenKey.CONTENT_SMS,
                    item.contentRawValue
                )

                Type.PHONE.toString() -> putString(
                    Constants.ScreenKey.CONTENT_PHONE,
                    item.contentRawValue
                )

                Type.LOCATION.toString() -> putString(
                    Constants.ScreenKey.CONTENT_LOCATION,
                    item.contentRawValue
                )

                Type.CONTACT.toString() -> putString(
                    Constants.ScreenKey.CONTENT_CONTACT,
                    item.contentRawValue
                )

                Type.BARCODE.toString() -> putString(
                    Constants.ScreenKey.CONTENT_TEXT,
                    item.contentRawValue
                )

                Type.FACEBOOK.toString(), Type.YOUTUBE.toString(), Type.INSTAGRAM.toString(), Type.TWITTER.toString(), Type.WHATSAPP.toString()
                    -> putString(Constants.ScreenKey.CONTENT_URL, item.contentRawValue)
            }
        }
        val intent = Intent(requireContext(), ScanResultActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        clearCacheDirectory(requireActivity(), "scan_qr")
    }

    private fun setSelectedScanned() {
        binding.ivScanSelected.visible()
        binding.ivCreateSelected.gone()
        historyType = HistoryType.SCANNED
        if (AppDatabase.getInstance(requireContext()).appDao()
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
        binding.ivScanSelected.gone()
        binding.ivCreateSelected.visible()
        historyType = HistoryType.CREATED
        if (AppDatabase.getInstance(requireContext()).appDao()
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
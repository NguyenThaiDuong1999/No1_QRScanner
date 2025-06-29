package com.scanqr.qrscanner.qrgenerator.feature.main.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.database.HistoryModel
import com.scanqr.qrscanner.qrgenerator.databinding.ItemHistoryBinding
import com.scanqr.qrscanner.qrgenerator.databinding.PopupMoreActionHistoryBinding
import com.scanqr.qrscanner.qrgenerator.feature.main.model.Type
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class HistoryAdapter(
    private var list: MutableList<HistoryModel>,
    private val onItemClick: (HistoryModel) -> Unit,
    private val onItemLongClick: (HistoryModel) -> Unit,
    private val onCopy: (HistoryModel) -> Unit,
    private val onShare: (HistoryModel) -> Unit,
    private val onDelete: (HistoryModel, Int) -> Unit,
    private val onSelect: (HistoryModel) -> Unit,
    private val onUnselect: (HistoryModel) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var isOnAction = false

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryModel) {
            if (item.isSelectMode) {
                binding.icMore.gone()
                binding.icSelect.visible()
            } else {
                binding.icMore.visible()
                binding.icSelect.gone()
            }
            if (item.isSelected) {
                binding.icSelect.setImageResource(R.drawable.ic_history_selected)
            } else {
                binding.icSelect.setImageResource(R.drawable.ic_history_unselect)
            }
            when (item.codeType) {
                Type.URL.toString() -> {
                    setDataType(binding, "#EBF5FF", R.drawable.ic_global, item, itemView.context.getString(R.string.url))
                }

                Type.LOCATION.toString() -> {
                    setDataType(binding, "#FFF0F0", R.drawable.ic_location, item, itemView.context.getString(R.string.location))
                }

                Type.CONTACT.toString() -> {
                    setDataType(binding, "#DEFEDD", R.drawable.ic_contact, item, itemView.context.getString(R.string.contact))
                }

                Type.TEXT.toString() -> {
                    setDataType(binding, "#EBEAFA", R.drawable.ic_text, item, itemView.context.getString(R.string.text))
                }

                Type.WIFI.toString() -> {
                    setDataType(binding, "#FFF5EB", R.drawable.ic_wifi, item, itemView.context.getString(R.string.wifi))
                }

                Type.PHONE.toString() -> {
                    setDataType(binding, "#FEE5E2", R.drawable.ic_phone, item, itemView.context.getString(R.string.phone))
                }

                Type.EMAIL.toString() -> {
                    setDataType(binding, "#E6F0FE", R.drawable.ic_email, item, itemView.context.getString(R.string.email))
                }

                Type.SMS.toString() -> {
                    setDataType(binding, "#D7FEEA", R.drawable.ic_sms, item, itemView.context.getString(R.string.sms))
                }

                Type.BARCODE.toString() -> {
                    setDataType(binding, "#FFF0DB", R.drawable.ic_barcode, item, itemView.context.getString(R.string.bar_code))
                }

                Type.FACEBOOK.toString() -> {
                    setDataType(binding, "#E2F1F8", R.drawable.ic_facebook, item, itemView.context.getString(R.string.facebook))
                }

                Type.INSTAGRAM.toString() -> {
                    setDataType(binding, "#FFE5EB", R.drawable.ic_instagram, item, itemView.context.getString(R.string.instagram))
                }

                Type.TWITTER.toString() -> {
                    setDataType(binding, "#FFF4E5", R.drawable.ic_twitter, item, itemView.context.getString(R.string.twitter))
                }

                Type.YOUTUBE.toString() -> {
                    setDataType(binding, "#FFE7E5", R.drawable.ic_youtube, item, itemView.context.getString(R.string.youtube))
                }

                Type.WHATSAPP.toString() -> {
                    setDataType(binding, "#DCFEED", R.drawable.ic_whatapps, item, itemView.context.getString(R.string.whats_app))
                }
            }
            binding.icMore.tap {
                val binding = PopupMoreActionHistoryBinding.inflate(LayoutInflater.from(itemView.context))
                val popupWindow = PopupWindow(
                    binding.root,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )
                binding.layoutCopy.tap {
                    popupWindow.dismiss()
                    onCopy.invoke(item)
                }
                binding.layoutShare.tap {
                    popupWindow.dismiss()
                    onShare.invoke(item)
                }
                binding.layoutDelete.tap {
                    popupWindow.dismiss()
                    onDelete.invoke(item, adapterPosition)
                }
                binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val values = IntArray(2)
                (it as ImageView).getLocationInWindow(values)
                val positionOfIcon = values[1] // lay toa do truc Y

                val displayMetrics: DisplayMetrics = itemView.context.resources.displayMetrics
                val height = displayMetrics.heightPixels * 4 / 5
                if (positionOfIcon > height) {
                    popupWindow.showAsDropDown(
                        it,
                        -itemView.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp),
                        (it.getHeight() - itemView.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp)),
                        Gravity.BOTTOM or Gravity.END
                    )
                } else {
                    Log.i("kkk", "<")
                    popupWindow.showAsDropDown(
                        it,
                        -itemView.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp),
                        -itemView.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._28sdp),
                        Gravity.TOP or Gravity.END
                    )
                }
            }
            binding.icSelect.tap {
                item.isSelected = !item.isSelected
                if (item.isSelected) {
                    binding.icSelect.setImageResource(R.drawable.ic_history_selected)
                    onSelect.invoke(item)
                } else {
                    binding.icSelect.setImageResource(R.drawable.ic_history_unselect)
                    onUnselect.invoke(item)
                }
            }
            itemView.tap {
                if (!isOnAction) {
                    isOnAction = true
                    onItemClick.invoke(item)
                    Handler(Looper.getMainLooper()).postDelayed({
                        isOnAction = false
                    }, 1500)
                }
            }
            itemView.setOnLongClickListener {
                onItemLongClick.invoke(item)
                return@setOnLongClickListener true
            }
        }
    }

    fun isSelectAll(): Boolean {
        var count = 0
        list.forEach {
            if (it.isSelected) {
                count++
            }
        }
        return count == list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.HistoryViewHolder {
        return HistoryViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setDataType(binding: ItemHistoryBinding, cardColor: String, cardItem: Int, model: HistoryModel, typeShow: String) {
        binding.cvType.setCardBackgroundColor(Color.parseColor(cardColor))
        binding.ivType.setImageResource(cardItem)
        binding.tvContent.text = model.contentRawValue
        binding.tvType.text = typeShow
        binding.tvTimeHour.text = model.timeHour
        binding.tvTimeDate.text = model.timeDate
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedModeOn() {
        for (item in list) {
            item.isSelectMode = true
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<HistoryModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectAll() {
        for (item in list) {
            item.isSelected = true
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateUnselectAll() {
        for (item in list) {
            item.isSelected = false
            item.isSelectMode = false
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedModeOff() {
        for (item in list) {
            item.isSelectMode = false
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectedItem(id: Int) {
        for (item in list) {
            if (item.id == id) {
                item.isSelected = true
            }
        }
        notifyDataSetChanged()
    }

}
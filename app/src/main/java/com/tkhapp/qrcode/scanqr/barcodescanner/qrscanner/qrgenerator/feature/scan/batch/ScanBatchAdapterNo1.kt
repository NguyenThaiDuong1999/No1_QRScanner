package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.scan.batch

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.database.HistoryModel
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ItemBatchNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.model.Type
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class ScanBatchAdapterNo1(
    private var list: ArrayList<HistoryModel>,
    private val onItemClick: (HistoryModel) -> Unit,
    private val onItemLongClick: (HistoryModel, Int) -> Unit,
    private val onDelete: (HistoryModel, Int) -> Unit,
    private val onSelect: (HistoryModel, Int) -> Unit,
    private val onUnselect: (HistoryModel, Int) -> Unit
) : RecyclerView.Adapter<ScanBatchAdapterNo1.ViewHolder>() {

    private var isOnAction = false

    inner class ViewHolder(private val binding: ItemBatchNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryModel) {
            binding.tvType.text = item.codeType
            binding.tvContent.text = item.contentRawValue
            if (item.isSelectMode) {
                binding.icDelete.gone()
                binding.icSelect.visible()
            } else {
                binding.icDelete.visible()
                binding.icSelect.gone()
            }
            if (item.isSelected) {
                binding.icSelect.setImageResource(R.drawable.ic_history_selected)
            } else {
                binding.icSelect.setImageResource(R.drawable.ic_history_unselect)
            }
            when (item.codeType) {
                Type.URL.toString() -> {
                    setDataType(
                        binding, "#EBF5FF", R.drawable.ic_global, item, itemView.context.getString(
                            R.string.url
                        )
                    )
                }

                Type.LOCATION.toString() -> {
                    setDataType(
                        binding, "#FFF0F0", R.drawable.ic_location, item, itemView.context.getString(
                            R.string.location
                        )
                    )
                }

                Type.CONTACT.toString() -> {
                    setDataType(
                        binding, "#DEFEDD", R.drawable.ic_contact, item, itemView.context.getString(
                            R.string.contact
                        )
                    )
                }

                Type.TEXT.toString() -> {
                    setDataType(
                        binding, "#EBEAFA", R.drawable.ic_text, item, itemView.context.getString(
                            R.string.text
                        )
                    )
                }

                Type.WIFI.toString() -> {
                    setDataType(
                        binding, "#FFF5EB", R.drawable.ic_wifi, item, itemView.context.getString(
                            R.string.wifi
                        )
                    )
                }

                Type.PHONE.toString() -> {
                    setDataType(
                        binding, "#FEE5E2", R.drawable.ic_phone, item, itemView.context.getString(
                            R.string.phone
                        )
                    )
                }

                Type.EMAIL.toString() -> {
                    setDataType(
                        binding, "#E6F0FE", R.drawable.ic_email, item, itemView.context.getString(
                            R.string.email
                        )
                    )
                }

                Type.SMS.toString() -> {
                    setDataType(
                        binding, "#D7FEEA", R.drawable.ic_sms, item, itemView.context.getString(
                            R.string.sms
                        )
                    )
                }

                Type.BARCODE.toString() -> {
                    setDataType(
                        binding, "#FFF0DB", R.drawable.ic_barcode, item, itemView.context.getString(
                            R.string.bar_code
                        )
                    )
                }

                Type.FACEBOOK.toString() -> {
                    setDataType(
                        binding, "#E2F1F8", R.drawable.ic_facebook, item, itemView.context.getString(
                            R.string.facebook
                        )
                    )
                }

                Type.INSTAGRAM.toString() -> {
                    setDataType(
                        binding, "#FFE5EB", R.drawable.ic_instagram, item, itemView.context.getString(
                            R.string.instagram
                        )
                    )
                }

                Type.TWITTER.toString() -> {
                    setDataType(
                        binding, "#FFF4E5", R.drawable.ic_twitter, item, itemView.context.getString(
                            R.string.twitter
                        )
                    )
                }

                Type.YOUTUBE.toString() -> {
                    setDataType(
                        binding, "#FFE7E5", R.drawable.ic_youtube, item, itemView.context.getString(
                            R.string.youtube
                        )
                    )
                }

                Type.WHATSAPP.toString() -> {
                    setDataType(
                        binding, "#DCFEED", R.drawable.ic_whatapps, item, itemView.context.getString(
                            R.string.whats_app
                        )
                    )
                }
            }
            binding.icSelect.tap {
                item.isSelected = !item.isSelected
                if (item.isSelected) {
                    binding.icSelect.setImageResource(R.drawable.ic_history_selected)
                    onSelect.invoke(item, adapterPosition)
                } else {
                    binding.icSelect.setImageResource(R.drawable.ic_history_unselect)
                    onUnselect.invoke(item, adapterPosition)
                }
            }
            itemView.setOnLongClickListener {
                onItemLongClick.invoke(item, adapterPosition)
                return@setOnLongClickListener true
            }
            itemView.tap {
                if (!isOnAction) {
                    isOnAction = true
                    onItemClick.invoke(item)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    isOnAction = false
                }, 1500)
            }
            binding.icDelete.tap {
                onDelete.invoke(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBatchNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    private fun setDataType(binding: ItemBatchNo1Binding, cardColor: String, cardItem: Int, model: HistoryModel, typeShow: String) {
        binding.cvType.setCardBackgroundColor(Color.parseColor(cardColor))
        binding.ivType.setImageResource(cardItem)
        binding.tvContent.text = model.contentRawValue
        binding.tvType.text = typeShow
    }

    fun updateList(list: ArrayList<HistoryModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedModeOn() {
        for (item in list) {
            item.isSelectMode = true
        }
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
    fun setSelectedModeOff() {
        for (item in list) {
            item.isSelectMode = false
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectedItem(content: String) {
        for (item in list) {
            if (item.contentRawValue == content) {
                item.isSelected = true
            }
        }
        notifyDataSetChanged()
    }

}
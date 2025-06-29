package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.background

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ItemBackgroundNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class BackgroundAdapterNo1(
    private val context: Context,
    private val list: MutableList<BackgroundModelNo1>,
    private val onClickItem: (BackgroundModelNo1) -> Unit,
    private val onClickNone: () -> Unit
) : RecyclerView.Adapter<BackgroundAdapterNo1.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBackgroundNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(backgroundModelNo1: BackgroundModelNo1, position: Int) {
            if (backgroundModelNo1.backgroundImage != null) {
                Glide.with(context).load(backgroundModelNo1.backgroundImage).into(binding.imgBackground)
            } else {
                Glide.with(context).load(R.drawable.img_none_backround_color).into(binding.imgBackground)
            }
            binding.root.tap {
                if (backgroundModelNo1.backgroundImage != null) {
                    onClickItem.invoke(backgroundModelNo1)
                    selectItem(backgroundModelNo1, position)
                } else {
                    onClickNone.invoke()
                    selectNone()
                }
            }
            if (backgroundModelNo1.isSelect) {
                binding.viewSelect.visible()
            } else {
                binding.viewSelect.gone()
            }
        }
    }

    private fun selectNone() {
        var indexSelectOld = -1
        list.forEachIndexed { index, backgroundModel ->
            if (backgroundModel.isSelect) {
                indexSelectOld = index
                backgroundModel.isSelect = false
            }
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
    }

    private fun selectItem(backgroundModelNo1: BackgroundModelNo1, position: Int) {
        var indexSelectOld = -1
        list.forEachIndexed { index, backgroundModel ->
            if (backgroundModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.backgroundImage == backgroundModelNo1.backgroundImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyItemChanged(position)
    }

    fun selectItem(backgroundModelNo1: BackgroundModelNo1) {
        val indexSelectOld = list.indexOfFirst { it.isSelect } // Find the old selection
        val indexSelectNew = list.indexOfFirst { it.backgroundImage == backgroundModelNo1.backgroundImage }

        // Update selection states without modifying the list while iterating
        list.forEach { it.isSelect = false }
        if (indexSelectNew >= 0) {
            list[indexSelectNew].isSelect = true
        }

        // Notify only affected items
        if (indexSelectOld >= 0) notifyItemChanged(indexSelectOld)
        if (indexSelectNew >= 0) notifyItemChanged(indexSelectNew)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBackgroundNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.post {
            val width = binding.root.width
            val layoutParams = binding.root.layoutParams
            layoutParams.height = width
            binding.root.layoutParams = layoutParams
        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

}
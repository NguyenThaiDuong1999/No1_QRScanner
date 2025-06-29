package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.template.color

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ItemColorNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class ColorAdapterNo1(
    private val context: Context,
    private val list: MutableList<ColorModelNo1>,
    private val onClickItem: (ColorModelNo1) -> Unit,
    private val onClickNone: () -> Unit
) : RecyclerView.Adapter<ColorAdapterNo1.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemColorNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(colorModelNo1: ColorModelNo1, position: Int) {
            if (position != 0) {
                binding.imgColor.setBackgroundColor(Color.parseColor(colorModelNo1.colorImage))
            } else {
                Glide.with(context).load(R.drawable.img_none_backround_color).into(binding.imgColor)
            }
            binding.root.tap {
                if (position != 0) {
                    onClickItem.invoke(colorModelNo1)
                    selectItem(colorModelNo1, position)
                } else {
                    onClickNone.invoke()
                    selectNone()
                }
            }
            if (colorModelNo1.isSelect) {
                binding.viewSelect.visible()
            } else {
                binding.viewSelect.gone()
            }
        }
    }

    private fun selectNone() {
        var indexSelectOld = -1
        list.forEachIndexed { index, colorModel ->
            if (colorModel.isSelect) {
                indexSelectOld = index
                colorModel.isSelect = false
            }
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
    }

    private fun selectItem(colorModelNo1: ColorModelNo1, position: Int) {
        var indexSelectOld = -1
        list.forEachIndexed { index, colorModel ->
            if (colorModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.colorImage == colorModelNo1.colorImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyItemChanged(position)
    }

    fun selectItem(colorModelNo1: ColorModelNo1) {
        var indexSelectOld = -1
        list.forEachIndexed { index, colorModel ->
            if (colorModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.colorImage == colorModelNo1.colorImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemColorNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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
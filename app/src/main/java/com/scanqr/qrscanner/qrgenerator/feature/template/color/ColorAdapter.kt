package com.scanqr.qrscanner.qrgenerator.feature.template.color

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemColorBinding
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class ColorAdapter(
    private val context: Context,
    private val list: MutableList<ColorModel>,
    private val onClickItem: (ColorModel) -> Unit,
    private val onClickNone: () -> Unit
) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(colorModel: ColorModel, position: Int) {
            if (position != 0) {
                binding.imgColor.setBackgroundColor(Color.parseColor(colorModel.colorImage))
            } else {
                Glide.with(context).load(R.drawable.img_none_backround_color).into(binding.imgColor)
            }
            binding.root.tap {
                if (position != 0) {
                    onClickItem.invoke(colorModel)
                    selectItem(colorModel, position)
                } else {
                    onClickNone.invoke()
                    selectNone()
                }
            }
            if (colorModel.isSelect) {
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

    private fun selectItem(colorModel: ColorModel, position: Int) {
        var indexSelectOld = -1
        list.forEachIndexed { index, colorModel ->
            if (colorModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.colorImage == colorModel.colorImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyItemChanged(position)
    }

    fun selectItem(colorModel: ColorModel) {
        var indexSelectOld = -1
        list.forEachIndexed { index, colorModel ->
            if (colorModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.colorImage == colorModel.colorImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
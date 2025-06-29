package com.scanqr.qrscanner.qrgenerator.feature.template.background

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemBackgroundBinding
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class BackgroundAdapter(
    private val context: Context,
    private val list: MutableList<BackgroundModel>,
    private val onClickItem: (BackgroundModel) -> Unit,
    private val onClickNone: () -> Unit
) : RecyclerView.Adapter<BackgroundAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBackgroundBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(backgroundModel: BackgroundModel, position: Int) {
            if (backgroundModel.backgroundImage != null) {
                Glide.with(context).load(backgroundModel.backgroundImage).into(binding.imgBackground)
            } else {
                Glide.with(context).load(R.drawable.img_none_backround_color).into(binding.imgBackground)
            }
            binding.root.tap {
                if (backgroundModel.backgroundImage != null) {
                    onClickItem.invoke(backgroundModel)
                    selectItem(backgroundModel, position)
                } else {
                    onClickNone.invoke()
                    selectNone()
                }
            }
            if (backgroundModel.isSelect) {
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

    private fun selectItem(backgroundModel: BackgroundModel, position: Int) {
        var indexSelectOld = -1
        list.forEachIndexed { index, backgroundModel ->
            if (backgroundModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.backgroundImage == backgroundModel.backgroundImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyItemChanged(position)
    }

    fun selectItem(backgroundModel: BackgroundModel) {
        val indexSelectOld = list.indexOfFirst { it.isSelect } // Find the old selection
        val indexSelectNew = list.indexOfFirst { it.backgroundImage == backgroundModel.backgroundImage }

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
        val binding = ItemBackgroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
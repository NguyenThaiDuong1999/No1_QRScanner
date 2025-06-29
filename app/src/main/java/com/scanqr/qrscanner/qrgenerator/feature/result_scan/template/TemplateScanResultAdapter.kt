package com.scanqr.qrscanner.qrgenerator.feature.result_scan.template

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemTemplateBinding
import com.scanqr.qrscanner.qrgenerator.feature.template.template_sub.TemplateModel
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class TemplateScanResultAdapter(
    private val context: Context,
    private val list: MutableList<TemplateModel>,
    private val onClickItem: (TemplateModel) -> Unit
) : RecyclerView.Adapter<TemplateScanResultAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTemplateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(templateModel: TemplateModel, position: Int) {
            templateModel.templateImage?.let { binding.rl.setBackgroundResource(it) }
            Glide.with(context).load(R.drawable.img_qr_template).into(binding.imgTemplate)
            binding.cv.visible()
            if (templateModel.isSelect) {
                binding.rlSelect.visible()
                binding.blurView.setupWith(binding.root)
                    .setFrameClearDrawable(binding.blurView.background)
                    .setBlurRadius(2f)
            } else {
                binding.rlSelect.gone()
            }
            binding.root.tap {
                onClickItem.invoke(templateModel)
                selectItem(templateModel, position)
            }
        }
    }

    fun selectItem(templateModel: TemplateModel, position: Int) {
        var indexSelectOld = -1
        list.forEachIndexed { index, logoModel ->
            if (logoModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.templateImage == templateModel.templateImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyItemChanged(position)
    }

    fun selectItem(templateModel: TemplateModel) {
        var indexSelectOld = -1
        list.forEachIndexed { index, logoModel ->
            if (logoModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.templateImage == templateModel.templateImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyDataSetChanged()
    }

    fun unSelectAllItem() {
        list.forEach {
            it.isSelect = false
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
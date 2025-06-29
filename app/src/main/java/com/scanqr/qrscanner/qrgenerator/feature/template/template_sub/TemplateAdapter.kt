package com.scanqr.qrscanner.qrgenerator.feature.template.template_sub

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemTemplateBinding
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class TemplateAdapter(
    private val context: Context,
    private val list: MutableList<TemplateModel>,
    private val onClickItem: (TemplateModel) -> Unit,
    private val onClickNone: () -> Unit,
    private val onClickPickImage: () -> Unit,
) : RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTemplateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(templateModel: TemplateModel, position: Int) {
            when (position) {
                0 -> {
                    binding.ct.setBackgroundResource(R.drawable.img_none_backround_color)
                    binding.cv.gone()
                    binding.rlSelect.gone()
                }

                1 -> {
                    binding.ct.setBackgroundResource(R.drawable.img_pick_image_logo)
                    binding.cv.gone()
                    binding.rlSelect.gone()
                }

                else -> {
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
                }
            }
            binding.root.tap {
                when (position) {
                    0 -> {
                        onClickNone.invoke()
                        selectNone()
                    }

                    1 -> {
                        onClickPickImage.invoke()
                    }

                    else -> {
                        onClickItem.invoke(templateModel)
                        selectItem(templateModel, position)
                    }
                }
            }
        }
    }

    fun selectNone() {
        var indexSelectOld = -1
        list.forEachIndexed { index, templateModel ->
            if (templateModel.isSelect) {
                indexSelectOld = index
                templateModel.isSelect = false
            }
        }
        if (indexSelectOld >= 2) {
            notifyItemChanged(indexSelectOld)
        }
    }

    private fun selectItem(templateModel: TemplateModel, position: Int) {
        var indexSelectOld = -1
        list.forEachIndexed { index, logoModel ->
            if (logoModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.templateImage == templateModel.templateImage
        }
        if (indexSelectOld >= 2) {
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
        if (indexSelectOld >= 2) {
            notifyItemChanged(indexSelectOld)
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
package com.scanqr.qrscanner.qrgenerator.feature.template.logo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemLogoNo1Binding
import com.scanqr.qrscanner.qrgenerator.utils.tap

class LogoAdapterNo1(
    private val context: Context,
    private val list: MutableList<LogoModelNo1>,
    private val onClickItem: (LogoModelNo1) -> Unit,
    private val onClickNone: () -> Unit
) : RecyclerView.Adapter<LogoAdapterNo1.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLogoNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(logoModelNo1: LogoModelNo1, position: Int) {
            if (position != 0) {
                logoModelNo1.logoImage?.let { binding.imgLogo.setImageResource(it) }
                //Glide.with(context).load(logoModel.logoImage).into(binding.imgLogo)
                if (logoModelNo1.isSelect) {
                    binding.rlSelect.visibility = View.VISIBLE
                    binding.blurView.setupWith(binding.root)
                        .setFrameClearDrawable(binding.blurView.background)
                        .setBlurRadius(2f)
                } else {
                    binding.rlSelect.visibility = View.GONE
                }
            } else {
                binding.imgLogo.setImageResource(R.drawable.img_none_logo)
                //Glide.with(context).load(R.drawable.img_none_logo).into(binding.imgLogo)
                binding.rlSelect.visibility = View.GONE
            }
            binding.root.tap {
                if (position != 0) {
                    onClickItem.invoke(logoModelNo1)
                    selectItem(logoModelNo1, position)
                } else {
                    onClickNone.invoke()
                    selectNone()
                }
            }
        }
    }

    private fun selectNone() {
        var indexSelectOld = -1
        list.forEachIndexed { index, logoModel ->
            if (logoModel.isSelect) {
                indexSelectOld = index
                logoModel.isSelect = false
            }
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
    }

    private fun selectItem(logoModelNo1: LogoModelNo1, position: Int) {
        var indexSelectOld = -1
        list.forEachIndexed { index, logoModel ->
            if (logoModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.logoImage == logoModelNo1.logoImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyItemChanged(position)
    }

    fun selectItem(logoModelNo1: LogoModelNo1) {
        var indexSelectOld = -1
        list.forEachIndexed { index, logoModel ->
            if (logoModel.isSelect) {
                indexSelectOld = index
            }
        }
        list.forEach {
            it.isSelect = it.logoImage == logoModelNo1.logoImage
        }
        if (indexSelectOld >= 0) {
            notifyItemChanged(indexSelectOld)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLogoNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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
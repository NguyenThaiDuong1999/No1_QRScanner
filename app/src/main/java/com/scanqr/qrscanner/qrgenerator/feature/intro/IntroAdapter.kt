package com.scanqr.qrscanner.qrgenerator.feature.intro

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.databinding.ItemGuideBinding
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.visible

class IntroAdapter(
    private val list: MutableList<IntroModel>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class IntroDefaultVH(val binding: ItemGuideBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IntroDefaultVH(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val introModel = list[position]
        val vh = holder as IntroDefaultVH
        vh.binding.apply {
            ivIntro.setImageResource(introModel.image)
            ivIntroFull.setImageResource(introModel.image)
            tvDesc.text = introModel.desc
            tvContent.text = introModel.content
        }
        if (position == 0) {
            vh.binding.ivIntro.visible()
            vh.binding.ivIntroFull.gone()
        } else {
            vh.binding.ivIntro.gone()
            vh.binding.ivIntroFull.visible()
        }
    }
}
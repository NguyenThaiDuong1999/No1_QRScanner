package com.scanqr.qrscanner.qrgenerator.feature.intro

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.databinding.ItemGuideNo1Binding

class IntroAdapterNo1(
    private val list: MutableList<IntroModelNo1>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class IntroDefaultVH(val binding: ItemGuideNo1Binding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemGuideNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            tvDesc.text = introModel.desc
            tvContent.text = introModel.content
        }
    }
}
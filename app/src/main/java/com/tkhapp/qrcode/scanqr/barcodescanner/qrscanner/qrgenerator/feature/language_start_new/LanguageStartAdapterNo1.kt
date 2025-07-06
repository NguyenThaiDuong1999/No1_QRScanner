package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language_start_new

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ItemLanguageNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.visible

class LanguageStartAdapterNo1(
    private val lists: List<LanguageModelNo1>,
    private val iClickLanguageNo1: IClickLanguageNo1,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isClicked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LanguageViewHolder(ItemLanguageNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = lists[position]
        if (holder is LanguageViewHolder) {
            holder.bind(data, position)
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    fun isSelectLanguage(): Boolean {
        lists.forEach {
            if (it.isCheck) {
                return true
            }
        }
        return false
    }

    inner class LanguageViewHolder(val binding: ItemLanguageNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LanguageModelNo1, position: Int) {
            data.image?.let { binding.civLogo.setImageResource(it) }
            binding.tvName.text = data.languageName

            binding.layoutItem.setOnClickListener { onItemClick(data) }

            if (data.isCheck) {
                binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_selected)
                binding.ivSelect.setImageResource(R.drawable.ic_lang_selected)
                binding.tvName.setTextColor(Color.parseColor("#2962FF"))
            } else {
                binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_unselected)
                binding.ivSelect.setImageResource(R.drawable.ic_lang_unselected)
                binding.tvName.setTextColor(Color.parseColor("#2C2C2E"))
            }

            if (position == 3 && !isClicked) {
                binding.animationView.visible()
                binding.animationView.playAnimation()
            } else {
                binding.animationView.gone()
                binding.animationView.pauseAnimation()
            }
        }

        private fun onItemClick(data: LanguageModelNo1) {
            isClicked = true
            iClickLanguageNo1.onClick(data)
            for (item in lists) {
                item.isCheck = item.languageName == data.languageName
            }
            notifyDataSetChanged()
        }
    }
}
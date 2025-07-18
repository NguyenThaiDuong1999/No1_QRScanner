package com.scanqr.qrscanner.qrgenerator.feature.language_start_new

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemLanguageBinding
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.visible

class LanguageStartNewAdapter(
    private val lists: List<LanguageModelNew>,
    private val iClickLanguage: IClickLanguage,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isClicked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LanguageViewHolder(ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LanguageModelNew, position: Int) {
            data.image?.let { binding.civLogo.setImageResource(it) }
            binding.tvName.text = data.languageName

            binding.layoutItem.setOnClickListener { onItemClick(data) }

            if (data.isCheck) {
                binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_selected)
                binding.ivSelect.setImageResource(R.drawable.ic_lang_selected)
                binding.tvName.setTextColor(Color.parseColor("#ffffff"))
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

        private fun onItemClick(data: LanguageModelNew) {
            isClicked = true
            iClickLanguage.onClick(data)
            for (item in lists) {
                item.isCheck = item.languageName == data.languageName
            }
            notifyDataSetChanged()
        }
    }
}
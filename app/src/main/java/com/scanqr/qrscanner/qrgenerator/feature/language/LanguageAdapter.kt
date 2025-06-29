package com.scanqr.qrscanner.qrgenerator.feature.language

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemLanguageBinding
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap

class LanguageAdapter(
    private val list: MutableList<LanguageModel>,
    private val onItemClick: (LanguageModel) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageModel) {
            if (item.isCheck) {
                binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_selected)
                binding.ivSelect.setImageResource(R.drawable.ic_lang_selected)
                binding.tvName.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_unselected)
                binding.ivSelect.setImageResource(R.drawable.ic_lang_unselected)
                binding.tvName.setTextColor(Color.parseColor("#2C2C2E"))
            }
            binding.tvName.text = item.languageName
            binding.civLogo.setImageResource(item.image)
            binding.animationView.gone()
            itemView.tap {
                onItemClick.invoke(item)
                selectLanguage(item)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectLanguage(languageModel: LanguageModel) {
        list.forEach {
            it.isCheck = false
        }
        list.forEach {
            if (it.isoLanguage == languageModel.isoLanguage) {
                it.isCheck = true
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setActive(locale: String) {
        for (lang in list) {
            lang.isCheck = lang.isoLanguage == locale
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(newList: ArrayList<LanguageModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}

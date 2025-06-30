package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.language

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.databinding.ItemLanguageNo1Binding
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.gone
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils.tap

class LanguageAdapterNo1(
    private val list: MutableList<LanguageModelNo1>,
    private val onItemClick: (LanguageModelNo1) -> Unit
) : RecyclerView.Adapter<LanguageAdapterNo1.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLanguageNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageModelNo1) {
            if (item.isCheck) {
                binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_selected)
                binding.ivSelect.setImageResource(R.drawable.ic_lang_selected)
                binding.tvName.setTextColor(Color.parseColor("#2962FF"))
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
    fun selectLanguage(languageModelNo1: LanguageModelNo1) {
        list.forEach {
            it.isCheck = false
        }
        list.forEach {
            if (it.isoLanguage == languageModelNo1.isoLanguage) {
                it.isCheck = true
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLanguageNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
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
    fun addList(newList: ArrayList<LanguageModelNo1>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}

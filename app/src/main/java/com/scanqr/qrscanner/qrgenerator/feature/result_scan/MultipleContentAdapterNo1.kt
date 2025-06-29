package com.scanqr.qrscanner.qrgenerator.feature.result_scan

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.databinding.ItemContentQrNo1Binding

class MultipleContentAdapterNo1(
    private val list: MutableList<String>
) : RecyclerView.Adapter<MultipleContentAdapterNo1.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemContentQrNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {
            val parts = str.split(":", limit = 2)
            if (parts.size == 2) {
                val before = parts[0].trim()
                val after = parts[1].trim()

                val title = "$before:"
                val content = "\"$after\"."

                val spannableBuilder = SpannableStringBuilder()
                val titleSpan = ForegroundColorSpan(Color.parseColor("#8E8E93"))
                spannableBuilder.append(title, titleSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                val contentSpan = ForegroundColorSpan(Color.parseColor("#1F1F29"))
                spannableBuilder.append(content, contentSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.tvMultipleContent.text = spannableBuilder
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemContentQrNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

}
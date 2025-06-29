package com.scanqr.qrscanner.qrgenerator.feature.uninstall

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemAnswerNo1Binding

class UninstallAdapterNo1(
    private val onClick: (AnswerModelNo1, position: Int) -> Unit,
    private val list: MutableList<AnswerModelNo1>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class AnswerVH(private val binding: ItemAnswerNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: AnswerModelNo1) {
            binding.apply {
                tvName.text = root.context.getString(data.name)
                if (data.isSelected) {
                    rdSelect.setImageResource(R.drawable.rd_select_why)
                } else {
                    rdSelect.setImageResource(R.drawable.rd_un_select_why)
                }
                root.setOnClickListener {
                    onClick(data, adapterPosition)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectAnswer(answerModelNo1: AnswerModelNo1) {
        list.forEach {
            it.isSelected = false
        }
        answerModelNo1.isSelected = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnswerVH(ItemAnswerNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AnswerVH) {
            holder.bindData(list[position])
        }
    }
}
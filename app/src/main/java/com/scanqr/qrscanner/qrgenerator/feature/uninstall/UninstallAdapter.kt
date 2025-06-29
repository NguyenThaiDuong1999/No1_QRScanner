package com.scanqr.qrscanner.qrgenerator.feature.uninstall

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.databinding.ItemAnswerBinding

class UninstallAdapter(
    private val onClick: (AnswerModel, position: Int) -> Unit,
    private val list: MutableList<AnswerModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class AnswerVH(private val binding: ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: AnswerModel) {
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
    fun selectAnswer(answerModel: AnswerModel) {
        list.forEach {
            it.isSelected = false
        }
        answerModel.isSelected = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnswerVH(ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AnswerVH) {
            holder.bindData(list[position])
        }
    }
}
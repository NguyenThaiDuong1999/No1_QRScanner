package com.scanqr.qrscanner.qrgenerator.feature.main.home.adapter

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scanqr.qrscanner.qrgenerator.databinding.ItemTypeScanNo1Binding
import com.scanqr.qrscanner.qrgenerator.feature.main.model.TypeScan
import com.scanqr.qrscanner.qrgenerator.utils.gone
import com.scanqr.qrscanner.qrgenerator.utils.tap
import com.scanqr.qrscanner.qrgenerator.utils.visible

class TypeScanAdapterNo1(
    private val list: ArrayList<TypeScan>,
    private val onItemClick: (TypeScan) -> Unit
) : RecyclerView.Adapter<TypeScanAdapterNo1.ViewHolder>() {

    private var isOnAction = false

    inner class ViewHolder(private val binding: ItemTypeScanNo1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TypeScan) {
            binding.apply {
                cvType.setCardBackgroundColor(Color.parseColor(item.bgColor))
                ivType.setImageResource(item.image)
                tvTypeName.text = item.name
                itemView.tap {
                    if (!isOnAction) {
                        isOnAction = true
                        onItemClick.invoke(item)
                        Handler(Looper.getMainLooper()).postDelayed({
                            isOnAction = false
                        }, 1500)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTypeScanNo1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

}
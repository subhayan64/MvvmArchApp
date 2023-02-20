package com.example.mvvmarchapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmarchapp.databinding.ItemGridComponentBinding
import com.example.mvvmarchapp.databinding.ItemsTileBinding
import com.example.mvvmarchapp.model.Item

class ItemListAdapter(private val items: List<Item>, var orientation: Int) :
    RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: View, val viewType: Int) :
        RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return if (orientation == 0) {
            ItemViewHolder(
                ItemsTileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).root, orientation
            )
        } else {
            ItemViewHolder(
                ItemGridComponentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).root, orientation
            )
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        when (holder.viewType) {
            0 -> {
                val binding = ItemsTileBinding.bind(holder.itemView)
                binding.tvItemTitle.text = currentItem.name
                binding.tvItemTPrice.text = currentItem.price
                if (currentItem.extra != null) {
                    binding.tvItemExtra.text = currentItem.extra
                } else {
                    binding.tvItemExtra.text = ""
                }
                //implement network image with Glide to appropriate image view
                Glide.with(binding.ivItems.context).load(currentItem.image).into(binding.ivItems)
            }
            1 -> {
                val binding = ItemGridComponentBinding.bind(holder.itemView)
                binding.tvItemTitle.text = currentItem.name
                binding.tvItemTPrice.text = currentItem.price

                Glide.with(binding.ivItems.context).load(currentItem.image).into(binding.ivItems)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}



package com.example.mvvmarchapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchapp.databinding.ItemsTileBinding
import com.example.mvvmarchapp.model.Item

class ItemListAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    inner class ItemListViewHolder(val binding: ItemsTileBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding = ItemsTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.binding.apply {
            tvItemTitle.text = items[position].name
            tvItemTPrice.text = items[position].price
            if (items[position].extra != null) {
                tvItemExtra.text = items[position].extra
            } else {
                tvItemExtra.text = ""
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}



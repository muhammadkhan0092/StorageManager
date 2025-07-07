package com.example.storemanager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storemanager.data.Item
import com.example.storemanager.databinding.ItemDownloadBinding

class DownloadAdapter(
    private val onItemClick: (Item) -> Unit
) : PagingDataAdapter<Item, DownloadAdapter.DownloadViewHolder>(DIFF_CALLBACK){

    inner class DownloadViewHolder(val binding: ItemDownloadBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadViewHolder {
        val binding = ItemDownloadBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DownloadViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DownloadViewHolder,
        position: Int
    ) {
        val currentItem = getItem(position)
        Log.d("khan","current item is ${currentItem?.barcode}")
        holder.binding.textView2.text = currentItem?.itemName
        holder.binding.root.setOnClickListener {
            onItemClick(currentItem!!)
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>(){
            override fun areItemsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem.itemId==newItem.itemId
            }

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}
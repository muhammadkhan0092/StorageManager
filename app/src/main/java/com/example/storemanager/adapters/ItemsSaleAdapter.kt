package com.example.storemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storemanager.data.ItemSalesSummary
import com.example.storemanager.databinding.ItemTransactionDetailBinding

class ItemsSaleAdapter(
) : RecyclerView.Adapter<ItemsSaleAdapter.ItemSaleViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemSaleViewHolder {
        val binding = ItemTransactionDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemSaleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemSaleViewHolder,
        position: Int
    ) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            tvItem.text = currentItem.name + " - " + currentItem.totalQuantity
            val a = currentItem.price * currentItem.totalQuantity.toInt().toDouble()
            tvPrice.text = a.toString()
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtil =object : DiffUtil.ItemCallback<ItemSalesSummary>(){
        override fun areItemsTheSame(
            oldItem: ItemSalesSummary,
            newItem: ItemSalesSummary
        ): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemSalesSummary,
            newItem: ItemSalesSummary
        ): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)

    inner class ItemSaleViewHolder(val binding: ItemTransactionDetailBinding) : RecyclerView.ViewHolder(binding.root){

    }
}
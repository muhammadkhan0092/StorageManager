package com.example.storemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storemanager.data.Item
import com.example.storemanager.data.PurchasedItemDetail
import com.example.storemanager.databinding.ItemTransactionBinding

class TransactionAdapter(
    private val onAddClicked: (PurchasedItemDetail) -> Unit,
    private val onSubtractClicked: (PurchasedItemDetail) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            tvItemName.text = currentItem.item.itemName
            tvItemQuantity.text = currentItem.item.quantityPerItem
            tvNoOfItems.text = currentItem.quantity.toString()
        }

        holder.binding.imgPlus.setOnClickListener {
            onAddClicked(currentItem)
        }
        holder.binding.imgMinus.setOnClickListener {
            onSubtractClicked(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtil =object : DiffUtil.ItemCallback<PurchasedItemDetail>(){
        override fun areItemsTheSame(
            oldItem: PurchasedItemDetail,
            newItem: PurchasedItemDetail
        ): Boolean {
            return oldItem.item==newItem.item
        }

        override fun areContentsTheSame(
            oldItem: PurchasedItemDetail,
            newItem: PurchasedItemDetail
        ): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)

    inner class TransactionViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root){

    }
}
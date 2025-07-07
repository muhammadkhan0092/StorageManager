package com.example.storemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storemanager.data.Item
import com.example.storemanager.data.PurchasedItemDetail
import com.example.storemanager.data.TransactionDetailAdapterData
import com.example.storemanager.data.TransactionDetails
import com.example.storemanager.databinding.ItemTransactionBinding
import com.example.storemanager.databinding.ItemTransactionDetailBinding

class TransactionDetailAdapter(
) : RecyclerView.Adapter<TransactionDetailAdapter.TransactionDetailViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionDetailViewHolder {
        val binding = ItemTransactionDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TransactionDetailViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TransactionDetailViewHolder,
        position: Int
    ) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            tvItem.text = currentItem.itemName + " - " + currentItem.quantityPerItem + " x " + currentItem.quantityPurchased
            val a = currentItem.price*currentItem.quantityPurchased.toInt().toDouble()
            tvPrice.text = a.toString()
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtil =object : DiffUtil.ItemCallback<TransactionDetailAdapterData>(){
        override fun areItemsTheSame(
            oldItem: TransactionDetailAdapterData,
            newItem: TransactionDetailAdapterData
        ): Boolean {
            return oldItem.itemId==newItem.itemId
        }

        override fun areContentsTheSame(
            oldItem: TransactionDetailAdapterData,
            newItem: TransactionDetailAdapterData
        ): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)

    inner class TransactionDetailViewHolder(val binding: ItemTransactionDetailBinding) : RecyclerView.ViewHolder(binding.root){

    }
}
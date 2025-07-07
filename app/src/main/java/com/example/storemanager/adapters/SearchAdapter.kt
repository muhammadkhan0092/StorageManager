package com.example.storemanager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storemanager.data.Item
import com.example.storemanager.data.TransactionWithUser
import com.example.storemanager.databinding.ItemDownloadBinding
import com.example.storemanager.databinding.ItemSearchTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchAdapter(
    private val onItemClick: (TransactionWithUser) -> Unit
) : PagingDataAdapter<TransactionWithUser, SearchAdapter.SearchViewHolder>(DIFF_CALLBACK){

    inner class SearchViewHolder(val binding: ItemSearchTransactionBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val binding = ItemSearchTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        val currentItem = getItem(position)
        holder.binding.tvPurchasedBy.text = "Purchased by : " + currentItem?.user?.userNo
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val date = sdf.format(Date(currentItem?.transaction?.data!!))
        holder.binding.tvDated.text = "Dated : "+ date
        holder.binding.tvTotalAmount.text = "Total Amount : " +currentItem?.transaction?.totalAmount.toString()
        holder.itemView.setOnClickListener {
            onItemClick(currentItem!!)
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionWithUser>(){
            override fun areItemsTheSame(
                oldItem: TransactionWithUser,
                newItem: TransactionWithUser
            ): Boolean {
                return oldItem.transaction.transactionId==newItem.transaction.transactionId
            }

            override fun areContentsTheSame(
                oldItem: TransactionWithUser,
                newItem: TransactionWithUser
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}
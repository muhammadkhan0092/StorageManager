package com.example.storemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storemanager.adapters.TransactionDetailAdapter
import com.example.storemanager.data.TransactionDetailAdapterData
import com.example.storemanager.databinding.FragmentTransactionDetailBinding
import com.example.storemanager.vm.TransactionDetailVm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentTransactionDetail : Fragment() {
    private  var binding : FragmentTransactionDetailBinding? = null
    private lateinit var adapter: TransactionDetailAdapter
    private val viewModel by viewModels<TransactionDetailVm>()
    private val navArgs by navArgs<FragmentTransactionDetailArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionDetailBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getData()
    }

    private fun setupAdapter() {
        adapter = TransactionDetailAdapter()
        binding!!.apply {
            rvDetails.adapter = adapter
            rvDetails.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun getData() {
        lifecycleScope.launch {
            val data = viewModel.getTransactionWithUserAndId(navArgs.transactionId.toLong())
            binding!!.textView3.text = data!!.transaction.totalAmount.toString()
            if(data!=null){
               val newList =  data.items.map {
                  TransactionDetailAdapterData(
                        it.itemId,it.itemName,it.quantityPerItem,
                        it.quantity.toString(),it.itemPrice)
                }
                adapter.differ.submitList(newList)
            }
            else
            {
                Toast.makeText(requireContext(), "Error Fetching Details", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
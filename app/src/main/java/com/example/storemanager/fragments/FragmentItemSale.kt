package com.example.storemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storemanager.adapters.ItemsSaleAdapter
import com.example.storemanager.databinding.FragmentItemsSaleBinding
import com.example.storemanager.databinding.FragmentSearchTransactionBinding
import com.example.storemanager.utils.VerticalItemDecoration
import com.example.storemanager.vm.ItemsSaleVm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentItemSale : Fragment() {
    private  var binding : FragmentItemsSaleBinding? = null
    private lateinit var adapter: ItemsSaleAdapter
    private var job : Job? =null
    private val viewModel by viewModels<ItemsSaleVm>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemsSaleBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getData()
    }



    private fun setupAdapter() {
        adapter = ItemsSaleAdapter()
        binding!!.apply {
            rvItemsSale.adapter = adapter
            rvItemsSale.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            rvItemsSale.addItemDecoration(VerticalItemDecoration(20))
        }
    }

    private fun getData() {
        lifecycleScope.launch {
            val data = viewModel.getUserWithTransaction()
            var revenue = 0.0
            data.forEach {
                revenue +=(it.price*it.totalQuantity.toDouble())
            }
            binding!!.textView5.text = revenue.toString()
            adapter.differ.submitList(data)
        }
    }


}
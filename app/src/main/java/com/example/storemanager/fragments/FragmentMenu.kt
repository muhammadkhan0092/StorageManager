package com.example.storemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.storemanager.R
import com.example.storemanager.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMenu : Fragment() {
    private var binding : FragmentMenuBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    private fun onClickListeners() {
        addItemListener()
        onDownloadBarcode()
        onNewTransactionClicked()
        onSearchTransaction()
        onItemsSaleListener()
    }

    private fun onItemsSaleListener() {
        binding!!.btnCheckSalesPerItem.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentItemSale)
        }
    }

    private fun onSearchTransaction() {
        binding!!.btnSearchUserTransactions.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentSearchTransaction)
        }
    }

    private fun onNewTransactionClicked() {
        binding!!.btnNewTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentNewTransaction)
        }
    }

    private fun onDownloadBarcode() {
        binding!!.btnDownloadBarcodes.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentDownloadBarcodes)
        }
    }

    private fun addItemListener() {
        binding!!.btnAddNewItem.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentAddItem)
        }
    }
}
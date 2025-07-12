package com.example.storemanager.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storemanager.R
import com.example.storemanager.adapters.SearchAdapter
import com.example.storemanager.data.TransactionWithUser
import com.example.storemanager.databinding.FragmentSearchTransactionBinding
import com.example.storemanager.utils.VerticalItemDecoration
import com.example.storemanager.vm.SearchTransactionVm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentSearchTransaction : Fragment() {
    private  var binding : FragmentSearchTransactionBinding? = null
    private lateinit var adapter: SearchAdapter
    private var job : Job? =null
    lateinit var allData : PagingData<TransactionWithUser>
    private val viewModel by viewModels<SearchTransactionVm>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchTransactionBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getData()
        onTextChanged()
    }

    private fun onTextChanged() {
        binding!!.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(600L)
                val query = editable.toString().trim()

                if (query.isNotEmpty()) {
                    binding!!.progressBar.visibility = View.VISIBLE
                    viewModel.getSpecificUserTransaction(query)
                        .collectLatest {
                            binding!!.progressBar.visibility = View.INVISIBLE
                            adapter.submitData(it)
                        }
                } else {
                    binding!!.progressBar.visibility = View.INVISIBLE
                    adapter.submitData(allData)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = SearchAdapter(
            {transactionWithUser->
                val data = Bundle().also {
                    it.putString("transactionId",transactionWithUser.transaction.transactionId.toString())
                }
                findNavController().navigate(R.id.action_fragmentSearchTransaction_to_fragmentTransactionDetail,data)
            }
        )
        binding!!.apply {
            rvSearch.adapter = adapter
            rvSearch.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            rvSearch.addItemDecoration(VerticalItemDecoration(30))
        }
    }

    private fun getData() {
        lifecycleScope.launch {
            viewModel.getUserWithTransaction().collectLatest {
                allData = it
                adapter.submitData(it)
            }
        }
    }


}
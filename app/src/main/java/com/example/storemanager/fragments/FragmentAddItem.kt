package com.example.storemanager.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.storemanager.data.Item
import com.example.storemanager.databinding.FragmentAddItemBinding
import com.example.storemanager.databinding.FragmentMenuBinding
import com.example.storemanager.utils.Resource
import com.example.storemanager.vm.AddItemVm
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentAddItem : Fragment() {
    private var binding : FragmentAddItemBinding? = null
    private val viewModel by viewModels<AddItemVm>()
    private var bitmap : Bitmap? = null
    private  var barcode : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateAndSetBitmap()
        onClickListeners()
        observers()
    }

    private fun observers() {
        onItemSetObserver()
    }



    private fun onItemSetObserver() {
        lifecycleScope.launch {
            viewModel.addItemFlow.collectLatest {
                when(it){
                    is Resource.Error<*> ->{
                        binding!!.progressBar2.visibility = View.INVISIBLE
                        binding!!.button.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                        Log.d("khan"," error ${it.message}")
                    }
                    is Resource.Loading<*> -> {
                        binding!!.button.visibility = View.INVISIBLE
                        binding!!.progressBar2.visibility = View.VISIBLE
                    }
                    is Resource.Success<*> -> {
                        Log.d("khan"," Item Set Up Successfull")
                        clearFields()
                        generateAndSetBitmap()
                        Toast.makeText(requireContext(), "Item Set Up Successfully", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Unspecified<*> -> {

                    }
                }
            }
        }
    }

    private fun clearFields(){
        binding!!.apply {
            etItemName.text.clear()
            etPrice.text.clear()
            etQuantity.text.clear()
        }
    }
    private fun onClickListeners() {
        onAddListener()
    }

    private fun onAddListener() {
        binding!!.apply {
            button.setOnClickListener {
                val itemName = etItemName.text.toString()
                val itemPrice = etPrice.text.toString()
                val quantity = etQuantity.text.toString()
                if(itemName.isBlank() || itemPrice.isBlank() || quantity.isBlank() || bitmap==null){
                    Toast.makeText(requireContext(), "Fill All Fields", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val item = Item(0,itemName,quantity,itemPrice.toDouble(),barcode!!)
                    Log.d("khan","setting barcode ${barcode!!}")
                    viewModel.insertItem(item)
                }
            }
        }
    }

    private fun generateAndSetBitmap() {
        barcode = generateBarcodeValue()
        Log.d("khan","barcode value is ${barcode}")
        bitmap = generateBarcodeBitmap(barcode!!)
        binding!!.progressBar2.visibility = View.INVISIBLE
        binding!!.button.visibility = View.VISIBLE
    }

    fun generateBarcodeValue(length: Int = 10): String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun generateBarcodeBitmap(data: String, width: Int = 200, height: Int = 200): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, width, height)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }
}
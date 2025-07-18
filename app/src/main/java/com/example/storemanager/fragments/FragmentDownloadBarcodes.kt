package com.example.storemanager.fragments

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storemanager.R
import com.example.storemanager.adapters.DownloadAdapter
import com.example.storemanager.data.Item
import com.example.storemanager.data.User
import com.example.storemanager.databinding.DialogEditItemBinding
import com.example.storemanager.databinding.DialogUserNoBinding
import com.example.storemanager.databinding.FragmentDownloadBarcodesBinding
import com.example.storemanager.databinding.FragmentMenuBinding
import com.example.storemanager.utils.VerticalItemDecoration
import com.example.storemanager.vm.DownloadBarcodesVm
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentDownloadBarcodes : Fragment() {
    private var binding : FragmentDownloadBarcodesBinding? = null
    private lateinit var adapter: DownloadAdapter
    private val viewModel by viewModels<DownloadBarcodesVm>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadBarcodesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeData()
    }

    private fun setupAdapter() {
        adapter = DownloadAdapter(
            { item ->
            Log.d("khan","Clicked On Click")
            val barcodeBitmap = generateBarcodeBitmap(item.barcode, 800, 300)
            saveBitmapToGallery(requireContext(), barcodeBitmap, item.itemName)
            },
            {
                showCustomDialog(it)
            },
            {
                showDeleteDialog(it)
            }
        )

        binding!!.rvBarcode.adapter = adapter
        binding!!.rvBarcode.addItemDecoration(VerticalItemDecoration(30))
        binding!!.rvBarcode.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
    }

    private fun showDeleteDialog(item: Item){
        AlertDialog.Builder(requireContext()) // Use `requireContext()` in a Fragment
            .setTitle("Title here")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("OK") { dialog, _ ->
                lifecycleScope.launch {
                    val result = viewModel.deleteItem(item)
                    if(result==0){
                        Toast.makeText(requireContext(), "Error Deleting Item", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Item Deleted Successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()


    }

    private fun showCustomDialog(item: Item) {
        val dialogBinding = DialogEditItemBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialog.show()
        dialogBinding.dialogButton.setOnClickListener {
            val quantity = dialogBinding.quantity.text.toString()
            val price = dialogBinding.price.text.toString()
            if(quantity.isEmpty() || price.isEmpty()){
                Toast.makeText(requireContext(), "Enter All Fields", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val newItem = item.copy(quantityPerItem = quantity, itemPrice = price.toDouble())
                lifecycleScope.launch {
                    val result = viewModel.editItem(newItem)
                    if(result==0){
                        Toast.makeText(requireContext(), "Update Failed", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Update Successfull", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.getDownloadingItem().collectLatest {
                adapter.submitData(it)
                Log.d("khan","data is ${it}")
            }
        }
    }

    fun generateBarcodeBitmap(data: String, width: Int = 200, height: Int = 200): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, width, height)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, name: String) {
        Log.d("khan","saving to gallery")
        val filename = "${name}_${System.currentTimeMillis()}.png"
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Barcodes")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }



}
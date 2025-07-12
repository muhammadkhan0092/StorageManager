package com.example.storemanager.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storemanager.adapters.TransactionAdapter
import com.example.storemanager.data.PurchasedItemDetail
import com.example.storemanager.data.Transaction
import com.example.storemanager.data.TransactionItem
import com.example.storemanager.data.User
import com.example.storemanager.databinding.DialogUserNoBinding
import com.example.storemanager.databinding.FragmentNewTransactionBinding
import com.example.storemanager.utils.BarcodeAnalyzer
import com.example.storemanager.utils.OrderResult
import com.example.storemanager.utils.VerticalItemDecoration
import com.example.storemanager.vm.NewTransactionVm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class FragmentNewTransaction : Fragment() {
    private  var binding : FragmentNewTransactionBinding? = null
    private lateinit var adapter: TransactionAdapter
    private var cameraProvider: ProcessCameraProvider? = null
    private var isProcessingBarcode = false
    private val viewModel by viewModels<NewTransactionVm>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTransactionBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPermissions()
        setupRecyclerView()
        onTransactionClicked()
    }

    private fun showCustomDialog() {
        // Inflate the custom layout using ViewBinding
        val dialogBinding = DialogUserNoBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialog.show()
        dialogBinding.dialogButton.setOnClickListener {
            val name = dialogBinding.userName.text.toString()
            val no = dialogBinding.userNo.text.toString()
            if(name.isEmpty() || no.isEmpty()){
                Toast.makeText(requireContext(), "Enter All Fields", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val user = User(no,name)
                applyTransaction(user)
                dialog.dismiss()
            }
        }
    }

    private fun applyTransaction(user: User){
        Log.d("khan","button clicked")
        val currentList = adapter.differ.currentList
        var totalAmount = 0.0
        currentList.forEach {
            totalAmount+=it.item.itemPrice * it.quantity
        }
        val transaction = Transaction(transactionId = 0, userNo = user.userNo, totalAmount = totalAmount)
        val transactionItems = currentList.map {
            TransactionItem(0,it.item.itemId,it.quantity)
        }

        lifecycleScope.launch {
            val result = viewModel.placeOrder(user,transaction,transactionItems)
            if(result is OrderResult.Success){
                Toast.makeText(requireContext(), "Transaction Successfull", Toast.LENGTH_SHORT).show()
                Log.d("khan","Transaction Successfull")
                findNavController().navigateUp()
            }
            else if(result is OrderResult.Failure)
            {
                Log.d("khan","Error ${result.message}")
                Toast.makeText(requireContext(), "Error ${result.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onTransactionClicked() {
        binding!!.button2.setOnClickListener {
            showCustomDialog()
        }
    }

    private fun onAddClicked(item: PurchasedItemDetail){
        val currentList = adapter.differ.currentList.toMutableList()
        currentList.remove(item)
        val newItem = item.copy(quantity = item.quantity+1)
        currentList.add(newItem)
        adapter.differ.submitList(currentList)
    }
    private fun onSubtractClicked(item: PurchasedItemDetail) {
        val currentList = adapter.differ.currentList.toMutableList()
        currentList.remove(item)
        if(item.quantity!=1){
            val newItem = item.copy(quantity = item.quantity-1)
            currentList.add(newItem)
        }
        adapter.differ.submitList(currentList)
    }
    private fun addItemToList(barcode : String){
        lifecycleScope.launch {
            val data = viewModel.getItem(barcode)
            if(data==null){
                Toast.makeText(requireContext(), "This Item Is Not Available", Toast.LENGTH_SHORT).show()
                Log.d("khan","item not available")
            }
            else
            {
                val currentList = adapter.differ.currentList.toMutableList()
                val a = currentList.filter {
                    it.item.itemId == data.itemId }
                if(a.isEmpty()){
                    Log.d("khan","item added first time")
                    playBeep()
                    currentList.add(PurchasedItemDetail(data,1))
                    adapter.differ.submitList(currentList)
                }
                else
                {
                    Toast.makeText(requireContext(), "Item Already Added . Increase Quantity", Toast.LENGTH_SHORT).show()
                }
            }
            delay(2000)
            isProcessingBarcode = false
        }
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(
            {
                onAddClicked(it)
            },
            {
                onSubtractClicked(it)
            }
        )
        binding!!.rv.adapter = adapter
        binding!!.rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding!!.rv.addItemDecoration(VerticalItemDecoration(30))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider?.unbindAll()
        cameraProvider = null
    }

    fun playBeep() {
        val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
    }
    private fun getPermissions() {
        if(ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }



    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding!!.previewView.surfaceProvider)
            }

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), BarcodeAnalyzer { result ->
                        result?.let { barcodeValue ->
                            if (!isProcessingBarcode) {
                                isProcessingBarcode = true
                                addItemToList(barcodeValue)
                            }
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(this, cameraSelector, preview, analyzer)

        }, ContextCompat.getMainExecutor(requireContext()))
    }


}
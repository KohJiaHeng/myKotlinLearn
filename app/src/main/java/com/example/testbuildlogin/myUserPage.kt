package com.example.testbuildlogin

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import java.io.InputStream
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testbuildlogin.databinding.UserpageBinding
import com.example.testbuildlogin.model.MyProduct
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MyUserPage : ComponentActivity() {
    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.findViewById(android.R.id.text1)
    }

    private lateinit var binding: UserpageBinding
    private val myArrayContent = arrayListOf(
        MyProduct(productName = "Apple", productPrice = 123),
        MyProduct(productName = "Samsung Galaxy", productPrice = 899),
    )

    private val barcodeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedBarcode = result.data?.getStringExtra("SCANNED_RESULT") ?: "Cannot decode barcode"
            addNewProduct(scannedBarcode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("test") ?: "User"
        binding.homeWelcomeTitle.text = "Welcome, $username"

        binding.myRecycleList.layoutManager = LinearLayoutManager(this)
        binding.myRecycleList.adapter = object : RecyclerView.Adapter<ProductViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                return ProductViewHolder(view)
            }

            override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
                val product = myArrayContent[position]
                holder.itemText.text = "${product.productName} — ${product.productPrice}"
            }

            override fun getItemCount(): Int = myArrayContent.size
        }

        binding.cameraButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            barcodeLauncher.launch(intent)
        }

        binding.barcodeButton.setOnClickListener {
            scanBarcodeFromAssets()
        }

        binding.logoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun scanBarcodeFromAssets() {
        try {
            val inputStream: InputStream = assets.open("myQR.jpeg")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val image = InputImage.fromBitmap(bitmap, 0)
            val scanner = BarcodeScanning.getClient()
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val resultText = if (barcodes.isNotEmpty()) {
                        barcodes[0].rawValue ?: "Empty barcode"
                    } else {
                        "Cannot decode barcode"
                    }
                    addNewProduct(resultText)
                }
                .addOnFailureListener {
                    addNewProduct("Cannot decode barcode")
                }
        } catch (e: Exception) {
            e.printStackTrace()
            addNewProduct("Error reading asset file")
        }
    }

    private fun addNewProduct(name: String) {
        myArrayContent.add(MyProduct(productName = name, productPrice = 0))
        binding.myRecycleList.adapter?.notifyItemInserted(myArrayContent.size - 1)
        binding.myRecycleList.scrollToPosition(myArrayContent.size - 1)
    }
}

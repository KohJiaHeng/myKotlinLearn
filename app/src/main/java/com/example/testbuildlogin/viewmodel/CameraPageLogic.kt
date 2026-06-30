package com.example.testbuildlogin
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testbuildlogin.databinding.CamerapageBinding
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage


class CameraPageLogic(
    private val activity: ComponentActivity,
    private val binding: CamerapageBinding
) {

    private val CAMERA_PERMISSION_CODE = 201

    fun testBarcodeWithStaticImage() {
        try {
            val options = BitmapFactory.Options().apply { inMutable = true }
            val bitmap = BitmapFactory.decodeResource(
                activity.resources,
                R.drawable.testbarcode,
                options
            )
            if (bitmap == null) {
                returnBarcodeResult("Error: Image file not found")
                return
            }

            val image = InputImage.fromBitmap(bitmap, 0)
            val scannerOptions = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
            val scanner = BarcodeScanning.getClient(scannerOptions)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isEmpty()) {
                        returnBarcodeResult("Scan complete: Barcode not found")
                    } else {
                        val scannedValue = barcodes[0].rawValue ?: "Empty text code payload"
                        returnBarcodeResult("Scanned: $scannedValue")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("ML_KIT_LOG", "Analysis failed processing", exception)
                    returnBarcodeResult("Scan complete: Scanner failed entirely")
                }

        } catch (e: Exception) {
            Log.e("ML_KIT_LOG", "Crash processing file stream", e)
            returnBarcodeResult("Scan error: Runtime crash")
        }
    }

    private fun returnBarcodeResult(resultMessage: String) {
        activity.runOnUiThread {
            val returnIntent = Intent().apply {
                putExtra("SCANNED_RESULT", resultMessage)
            }
            activity.setResult(Activity.RESULT_OK, returnIntent)
            activity.finish() // Closes CameraActivity and slides right back to MyUserPage layout view
        }
    }
    fun checkAndStartCamera() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCameraStream()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun startCameraStream() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(activity, cameraSelector, preview)

            } catch (exc: Exception) {
                Log.e("CAMERA_MANAGER", "CameraX initialization failed", exc)
            }
        }, ContextCompat.getMainExecutor(activity))
    }


    private fun handleBarcodeFound(barcodeValue: String) {
        activity.runOnUiThread {
            val returnIntent = Intent().apply {
                putExtra("SCANNED_RESULT", barcodeValue)
            }
            activity.setResult(Activity.RESULT_OK, returnIntent)
            activity.finish()
        }
    }
}
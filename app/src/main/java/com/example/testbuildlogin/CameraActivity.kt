package com.example.testbuildlogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.testbuildlogin.databinding.CamerapageBinding

class CameraActivity : ComponentActivity() {
    private lateinit var binding: CamerapageBinding
    private lateinit var cameraLogic: CameraPageLogic
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Whether permission is allowed or rejected, we process our image validation test!
        cameraLogic.testBarcodeWithStaticImage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CamerapageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraLogic = CameraPageLogic(this, binding)
        cameraLogic.checkAndStartCamera()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraLogic.checkAndStartCamera()
    }
}

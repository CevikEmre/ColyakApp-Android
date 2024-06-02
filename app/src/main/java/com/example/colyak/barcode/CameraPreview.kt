package com.example.colyak.barcode

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.screens.Screens
import com.example.colyak.viewmodel.BarcodeViewModel
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import readedBarcode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraPreview(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val barcodeVal = remember { mutableStateOf("") }
    val barcodeVM: BarcodeViewModel = viewModel()
    val scope = rememberCoroutineScope()
    var barcodeProcessed by remember { mutableStateOf(false) }

    AndroidView(
        factory = { androidViewContext ->
            PreviewView(androidViewContext).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { previewView ->
            val cameraSelector: CameraSelector =
                CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener(
                {
                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    val barcodeAnalyser = BarcodeAnalyzer { barcodes ->
                        if (!barcodeProcessed) {
                            barcodes.forEach { barcode ->
                                barcode.rawValue?.let { barcodeValue ->
                                    barcodeVal.value = barcodeValue
                                    barcodeProcessed = true
                                    scope.launch {
                                        try {
                                            barcodeVM.getBarcode(barcodeValue)
                                            readedBarcode = barcodeValue
                                            Toast.makeText(context, barcodeValue, Toast.LENGTH_SHORT).show()
                                            delay(1200)
                                            navController.navigate(Screens.BarcodeDetailScreen.screen)
                                        } catch (e: Exception) {
                                            Log.e("BARKOD_ERROR", "Error fetching barcode: ${e.localizedMessage}")
                                            Toast.makeText(context, "Barkod alınırken bir hata oluştu", Toast.LENGTH_SHORT).show()
                                            barcodeProcessed = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                        }
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        Log.e("CameraException", "cameraPreview ${e.localizedMessage}")
                    }
                },
                ContextCompat.getMainExecutor(context)
            )
        }
    )
}

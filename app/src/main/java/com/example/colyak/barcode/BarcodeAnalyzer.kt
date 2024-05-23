package com.example.colyak.barcode

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    private val onBarcodeDetected: (
        barcodes: List<Barcode>
    ) -> Unit,

    ) : ImageAnalysis.Analyzer {
    private var lastAnalyzedTimestamp = 0L

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >= java.util.concurrent.TimeUnit.SECONDS.toMillis(
                1
            )
        ) {
            image.image?.let { imageToAnalyze ->
                val options =
                    BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_EAN_13)
                        .build()
                val barcodeScanner = BarcodeScanning.getClient(options)
                val imageToProcess =
                    InputImage.fromMediaImage(imageToAnalyze, image.imageInfo.rotationDegrees)
                barcodeScanner.process(imageToProcess).addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        onBarcodeDetected(barcodes)
                    } else {
                        Log.e("BarcodeAnalyzer", "No barcode detected")
                    }
                }.addOnFailureListener { e ->
                    Log.e("BarcodeAnalyzer", "Error scanning barcode: ${e.message}")

                }.addOnCompleteListener {
                    image.close()
                }
            }
            lastAnalyzedTimestamp = currentTimestamp
        } else {
            image.close()
        }
    }
}
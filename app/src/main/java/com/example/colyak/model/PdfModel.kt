package com.example.colyak.model

import android.graphics.pdf.PdfDocument
import android.net.Uri

data class PdfModel(
    val documents: Map<Uri, PdfDocument> = emptyMap(),
    val selectedDocumentUri: Uri? = null
)

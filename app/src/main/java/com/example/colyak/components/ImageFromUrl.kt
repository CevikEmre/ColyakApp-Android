package com.example.colyak.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter


@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageFromUrl(url: String, modifier: Modifier) {
    val imagePainter = rememberImagePainter(
        data = url,
        builder = {
            crossfade(false)
            placeholder(android.R.drawable.ic_menu_gallery)
        }
    )

    Image(
        painter = imagePainter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds
    )
}


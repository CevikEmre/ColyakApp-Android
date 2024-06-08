package com.example.colyak.components.functions

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import coil.request.CachePolicy


@Composable
fun ImageFromUrl(url: String, modifier: Modifier) {
    val imagePainter = rememberImagePainter(
        data = url,
        builder = {
            crossfade(false)
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.stat_notify_error)
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            networkCachePolicy(CachePolicy.ENABLED)
        }
    )
    Image(
        painter = imagePainter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds
    )
}


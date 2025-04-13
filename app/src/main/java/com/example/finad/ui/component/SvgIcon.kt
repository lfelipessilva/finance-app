package com.example.finad.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.compose.AsyncImage

@Composable
fun SvgIcon(iconUrl: String, label: String, tint: Color, modifier: Modifier) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(iconUrl)
            .build(),
        contentDescription = label,
        imageLoader = imageLoader,
        colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null,
        modifier = modifier
    )
}

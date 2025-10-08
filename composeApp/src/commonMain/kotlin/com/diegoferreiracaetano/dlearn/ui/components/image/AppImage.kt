package com.diegoferreiracaetano.dlearn.ui.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppImage(
    imageURL: String? = null,
    imageResource: DrawableResource? = null,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {

    val painter = if (imageResource != null && imageURL.isNullOrEmpty())
        painterResource(imageResource)
    else
        rememberImagePainter(imageURL!!)

    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier.size(120.dp)
    )
}
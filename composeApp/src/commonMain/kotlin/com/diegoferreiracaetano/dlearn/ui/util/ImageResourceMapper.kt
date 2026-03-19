package com.diegoferreiracaetano.dlearn.ui.util

import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.ui.sdui.AppImageType
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.favorite
import dlearn.composeapp.generated.resources.search
import dlearn.composeapp.generated.resources.watchlist

fun AppImageType?.toImageSource(): AppImageSource? {
    val resource = when (this) {
        AppImageType.SEARCH -> Res.drawable.search
        AppImageType.EMPTY_STATE -> Res.drawable.search
        AppImageType.WATCHLIST -> Res.drawable.watchlist
        AppImageType.FAVORITE -> Res.drawable.favorite
        else -> null
    }
    return resource?.let { AppImageSource.Resource(it) }
}

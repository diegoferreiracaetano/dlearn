package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType

fun AppIconType?.toIcon(): ImageVector? {
    return when (this) {
        AppIconType.PERSON -> Icons.Default.Person
        AppIconType.LOCK -> Icons.Default.Lock
        AppIconType.NOTIFICATIONS -> Icons.Default.Notifications
        AppIconType.LANGUAGE -> Icons.Default.Language
        AppIconType.PUBLIC -> Icons.Default.Public
        AppIconType.DELETE -> Icons.Default.Delete
        AppIconType.POLICY -> Icons.Default.Policy
        AppIconType.HELP -> Icons.AutoMirrored.Filled.Help
        AppIconType.INFO -> Icons.Default.Info
        AppIconType.WORKSPACE_PREMIUM -> Icons.Default.WorkspacePremium
        AppIconType.PLAY_ARROW -> Icons.Default.PlayArrow
        AppIconType.DOWNLOAD -> Icons.Default.Download
        AppIconType.SHARE -> Icons.Default.Share
        AppIconType.CALENDAR_MONTH -> Icons.Default.CalendarMonth
        AppIconType.SCHEDULE -> Icons.Default.Schedule
        AppIconType.STAR -> Icons.Default.Star
        AppIconType.HOME -> Icons.Default.Home
        AppIconType.SEARCH -> Icons.Default.Search
        AppIconType.FAVORITE -> Icons.Default.Favorite
        AppIconType.WATCHLIST -> Icons.AutoMirrored.Filled.List
        AppIconType.UNKNOWN, null -> null
    }
}

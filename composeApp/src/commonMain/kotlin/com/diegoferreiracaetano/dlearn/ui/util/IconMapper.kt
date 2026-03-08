package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconMapper {
    private const val PERSON = "person"
    private const val LOCK = "lock"
    private const val NOTIFICATIONS = "notifications"
    private const val LANGUAGE = "language"
    private const val PUBLIC = "public"
    private const val DELETE = "delete"
    private const val POLICY = "policy"
    private const val HELP = "help"
    private const val INFO = "info"

    private val icons = mapOf(
        PERSON to Icons.Default.Person,
        LOCK to Icons.Default.Lock,
        NOTIFICATIONS to Icons.Default.Notifications,
        LANGUAGE to Icons.Default.Language,
        PUBLIC to Icons.Default.Public,
        DELETE to Icons.Default.Delete,
        POLICY to Icons.Default.Policy,
        HELP to Icons.AutoMirrored.Filled.Help,
        INFO to Icons.Default.Info
    )

    fun map(identifier: String?): ImageVector? = icons[identifier]
}

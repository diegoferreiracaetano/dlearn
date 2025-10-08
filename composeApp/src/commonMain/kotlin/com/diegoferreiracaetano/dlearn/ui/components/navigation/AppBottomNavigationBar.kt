package com.diegoferreiracaetano.dlearn.ui.components.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Fireplace
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.PlatformType
import com.diegoferreiracaetano.dlearn.currentPlatform
import com.diegoferreiracaetano.dlearn.isIOS
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.util.getLogger
import org.jetbrains.compose.ui.tooling.preview.Preview

data class AppBottomNavigation(
    val selectedRoute: String = tabList.first().route,
    val items: List<AppNavigationTab> = tabList,
    val onTabSelected: (String) -> Unit,
)

data class AppNavigationTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)


@Composable
fun AppBottomNavigationBar(
    items: List<AppNavigationTab>,
    selectedRoute: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

        NavigationBar(
            modifier = modifier.height(80.dp),
            windowInsets =
                if (isIOS) WindowInsets(bottom =  28.dp) else NavigationBarDefaults.windowInsets
        ) {

            val customColors: NavigationBarItemColors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )

            items.forEach { tab ->
                val isSelected = tab.route == selectedRoute
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            onTabSelected(tab.route)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.label,
                            tint = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    label = {
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = customColors
                )
            }
        }

}

enum class TabItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME(ScreenRouter.Home.route, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    Regions(
        ScreenRouter.Regions.route,
        "Novidades",
        Icons.Filled.Fireplace,
        Icons.Outlined.Fireplace
    ),
    Favorites(
        ScreenRouter.Favorites.route,
        "Favoritos",
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder
    ),
    Account(
        ScreenRouter.Profile.route,
        "Conta",
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle
    )
}

val tabList = TabItem.entries.map {
    AppNavigationTab(
        route = it.route,
        label = it.label,
        selectedIcon = it.selectedIcon,
        unselectedIcon = it.unselectedIcon
    )
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    DLearnTheme {
        AppBottomNavigationBar(
            items = tabList,
            selectedRoute = TabItem.HOME.route,
            onTabSelected = { selectedTab ->
                println("Selecionado: ${selectedTab}")
            }
        )
    }
}

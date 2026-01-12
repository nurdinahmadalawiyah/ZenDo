package com.dinzio.zendo.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.InsertChart
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Timer
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.core.navigation.ZenDoRoutes

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val navItems = listOf(
    BottomNavItem("Home", Icons.TwoTone.Home, ZenDoRoutes.Home.route),
    BottomNavItem("Focus", Icons.TwoTone.Timer, ZenDoRoutes.Focus.route),
    BottomNavItem("Stats", Icons.TwoTone.InsertChart, ZenDoRoutes.Stats.route),
    BottomNavItem("Settings", Icons.TwoTone.Settings, ZenDoRoutes.Settings.route)
)

@Composable
fun ZenDoBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        navItems.take(2).forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) },
                colors = navigationBarItemColors()
            )
        }

        NavigationBarItem(
            selected = false,
            onClick = { },
            enabled = false,
            icon = {
                Surface(
                    onClick = onPlusClick,
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.TwoTone.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            },
            label = null,
            alwaysShowLabel = false,
            colors = NavigationBarItemDefaults.colors(
                disabledIconColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = Color.Transparent
            )
        )

        navItems.takeLast(2).forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) },
                colors = navigationBarItemColors()
            )
        }
    }
}

@Composable
fun ZenDoNavigationRail(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPlusClick: () -> Unit
) {
    NavigationRail(
        modifier = modifier.padding(end = 8.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        header = {
            FloatingActionButton(
                onClick = onPlusClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(Icons.TwoTone.Add, contentDescription = "Add")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            navItems.forEach { item ->
                val isSelected = currentRoute == item.route
                NavigationRailItem(
                    selected = isSelected,
                    onClick = { onNavigate(item.route) },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(text = item.label) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@Preview(name = "Bottom Bar - Light Mode", showBackground = true)
@Composable
fun PreviewZenDoBottomBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        ZenDoBottomBar(
            currentRoute = "home",
            onPlusClick = {},
            onNavigate = {}
        )
    }
}

@Preview(name = "Navigation Rail - Tablet Mode", showBackground = true, heightDp = 600, widthDp = 100)
@Composable
fun PreviewZenDoNavigationRail() {
    ZenDoNavigationRail(
        currentRoute = "focus",
        onNavigate = {},
        onPlusClick = {},
        modifier = Modifier.fillMaxHeight()
    )
}

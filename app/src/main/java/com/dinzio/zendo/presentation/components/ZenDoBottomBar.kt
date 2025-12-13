package com.dinzio.zendo.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.core.theme.GrayText
import com.dinzio.zendo.core.theme.GreenPrimary
import com.dinzio.zendo.core.theme.White

// Data Menu (Dipakai Bersama)
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val navItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, "home"),
    BottomNavItem("Focus", Icons.Default.Timer, "focus"),
    BottomNavItem("Stats", Icons.Default.BarChart, "stats"),
    BottomNavItem("Profile", Icons.Default.Person, "profile")
)

// --- 1. COMPONENT BOTTOM BAR (Untuk HP Portrait) ---
@Composable
fun ZenDoBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = White,
        contentColor = GreenPrimary
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GreenPrimary,
                    selectedTextColor = GreenPrimary,
                    indicatorColor = GreenPrimary.copy(alpha = 0.1f),
                    unselectedIconColor = GrayText,
                    unselectedTextColor = GrayText
                )
            )
        }
    }
}

// --- 2. COMPONENT NAV RAIL (Untuk Landscape/Tablet) ---
@Composable
fun ZenDoNavigationRail(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.padding(end = 8.dp), // Sedikit jarak ke konten
        containerColor = White,
        contentColor = GreenPrimary,
        header = {
            // Opsional: Bisa taruh Logo ZenDo atau FAB (+) di sini
            // Icon(Icons.Default.Menu, contentDescription = null)
        }
    ) {
        // Posisikan item di tengah secara vertikal
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            navItems.forEach { item ->
                val isSelected = currentRoute == item.route
                NavigationRailItem(
                    selected = isSelected,
                    onClick = { onNavigate(item.route) },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(text = item.label) }, // Di NavRail label biasanya selalu muncul
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = GreenPrimary,
                        selectedTextColor = GreenPrimary,
                        indicatorColor = GreenPrimary.copy(alpha = 0.1f),
                        unselectedIconColor = GrayText,
                        unselectedTextColor = GrayText
                    )
                )
                Spacer(modifier = Modifier.height(16.dp)) // Jarak antar item
            }
        }
    }
}
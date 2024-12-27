package com.example.advancedinventory.presentation.component

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.advancedinventory.presentation.navigation.BottomNavItem
import com.example.advancedinventory.ui.theme.OrangePrimary
import com.example.advancedinventory.ui.theme.PrimaryWhite

@Composable
fun BottomNavItem(
    items: List<BottomNavItem>,
    navController: NavController,)
{
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    Log.d("BottomBar", "BottomNavItem called with items: $items")

    NavigationBar(
        modifier = Modifier,
        containerColor = PrimaryWhite
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (currentDestination == item.route) OrangePrimary else Color.Gray
                    )
                },
                label = {
                    Text(
                        item.title,
                        fontSize = 14.sp,
                        color = if (currentDestination == item.route) OrangePrimary else Color.Gray
                    )
                },
                selected = currentDestination == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

            )
        }
    }
    
}
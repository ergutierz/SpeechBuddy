package com.ignotusvia.speechbuddy.view

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ignotusvia.speechbuddy.core.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    navController: NavHostController,
    scaffoldState: ScaffoldState
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = Screen.getScreenTitle(currentRoute),
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        },
        navigationIcon = {
            NavigationIcon(
                currentRoute = currentRoute,
                navController = navController,
                scaffoldState = scaffoldState
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colors.primary,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
private fun NavigationIcon(
    currentRoute: String?,
    navController: NavHostController,
    scaffoldState: ScaffoldState
) {
    val coroutineScope = rememberCoroutineScope()
    when (currentRoute) {
        Screen.LoginScreen.route -> Unit
        Screen.DashboardScreen.route -> IconButton(onClick = {
            coroutineScope.launch {
                scaffoldState.drawerState.open()
            }
        }) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
        }
        else -> IconButton(onClick = {
            navController.navigate(Screen.DashboardScreen.route) {
                popUpTo(Screen.DashboardScreen.route) {
                    inclusive = false
                }
            }
        }) {
            Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
        }
    }
}
package com.essie.myads.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class HomeScreen(val icon: ImageVector) {

    OVERVIEW(icon = Icons.Filled.PieChart),
    SETTINGS(icon = Icons.Filled.Settings);

    companion object {
        fun fromRoute(route: String?): HomeScreen =
            when (route?.substringBefore("/")) {
                SETTINGS.name -> SETTINGS
                OVERVIEW.name -> OVERVIEW
                else -> OVERVIEW
            }
    }
}

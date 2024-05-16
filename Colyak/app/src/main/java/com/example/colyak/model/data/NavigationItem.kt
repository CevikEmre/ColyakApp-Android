package com.example.colyak.model.data

data class NavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val badgeCount: Int? = null
)

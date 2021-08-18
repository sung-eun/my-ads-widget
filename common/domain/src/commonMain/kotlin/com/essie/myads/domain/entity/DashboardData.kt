package com.essie.myads.domain.entity

data class DashboardData(
    val exposure: Long = 0,
    val click: Long = 0,
    val recentlyIncome: Long = 0,
    val dateRange: DateRange = DateRange.LAST_7DAYS,
    val totalBalance: Long = 0
)

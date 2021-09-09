package com.essie.myads.domain.entity

data class DashboardData(
    val impression: Long = 0,
    val click: Long = 0,
    val recentlyEstimatedIncome: Long = 0,
    val dateRange: DateRange = DateRange.LAST_7DAYS,
    val totalUnpaidAmount: String = "0"
)

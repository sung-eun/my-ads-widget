package com.essie.myads.domain.entity

data class DashboardData(
    val impressions: Long = 0,
    val clicks: Long = 0,
    val recentlyEstimatedIncome: String = "$0.00",
    val dateRange: DateRange = DateRange.LAST_7DAYS,
    val totalUnpaidAmount: String = "$0.00"
)

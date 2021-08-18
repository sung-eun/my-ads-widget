package com.essie.myads.domain.repository

import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange

interface IAdsRepository {
    suspend fun getAdsData(dateRange: DateRange): DashboardData
}
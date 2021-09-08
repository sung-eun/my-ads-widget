package com.essie.myads.domain.repository

import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange

interface IAdsRepository {
    suspend fun getAdsData(account: AdAccount, dateRange: DateRange): DashboardData
}
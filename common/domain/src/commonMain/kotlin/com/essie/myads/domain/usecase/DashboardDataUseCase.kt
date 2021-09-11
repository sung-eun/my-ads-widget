package com.essie.myads.domain.usecase

import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange
import com.essie.myads.domain.repository.IAdsRepository

class DashboardDataUseCase(private val repository: IAdsRepository) {
    suspend fun getDashboardData(
        account: AdAccount,
        dateRange: DateRange = DateRange.LAST_7DAYS
    ): DashboardData {
        return repository.getAdsData(account, dateRange)
    }

    suspend fun getDashboardData(
        accountName: String,
        dateRange: DateRange = DateRange.LAST_7DAYS
    ): DashboardData {
        return repository.getAdsData(accountName, dateRange)
    }
}